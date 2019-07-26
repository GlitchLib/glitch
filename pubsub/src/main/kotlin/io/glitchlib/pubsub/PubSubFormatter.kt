package io.glitchlib.pubsub

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.model.IEvent
import io.glitchlib.model.PingEvent
import io.glitchlib.model.PongEvent
import io.glitchlib.pubsub.events.BanUserEvent
import io.glitchlib.pubsub.events.BitsEvent
import io.glitchlib.pubsub.events.ChannelExtensionBroadcastEvent
import io.glitchlib.pubsub.events.ClearChatEvent
import io.glitchlib.pubsub.events.CommerceEvent
import io.glitchlib.pubsub.events.EmoteOnlyEvent
import io.glitchlib.pubsub.events.ErrorResponseEvent
import io.glitchlib.pubsub.events.FollowEvent
import io.glitchlib.pubsub.events.HostEvent
import io.glitchlib.pubsub.events.MessageDeleteEvent
import io.glitchlib.pubsub.events.PubSubEvent
import io.glitchlib.pubsub.events.ReconnectRequiredEvent
import io.glitchlib.pubsub.events.Robot9000Event
import io.glitchlib.pubsub.events.StreamDownEvent
import io.glitchlib.pubsub.events.StreamUpEvent
import io.glitchlib.pubsub.events.SubGiftEvent
import io.glitchlib.pubsub.events.SubscribersOnlyEvent
import io.glitchlib.pubsub.events.SubscriptionEvent
import io.glitchlib.pubsub.events.SuccessfulResponseEvent
import io.glitchlib.pubsub.events.TimeoutUserEvent
import io.glitchlib.pubsub.events.UnbanUserEvent
import io.glitchlib.pubsub.events.UnknownMessageEvent
import io.glitchlib.pubsub.events.ViewCountEvent
import io.glitchlib.pubsub.events.WhisperReceivedEvent
import io.glitchlib.pubsub.events.WhisperSentEvent
import io.glitchlib.pubsub.events.WhisperThreadEvent
import io.glitchlib.pubsub.model.MessageType
import io.glitchlib.pubsub.model.SubscriptionContext
import io.glitchlib.pubsub.model.TopicException
import io.glitchlib.pubsub.model.json.ActivationByMod
import io.glitchlib.pubsub.model.json.Ban
import io.glitchlib.pubsub.model.json.BitsMessage
import io.glitchlib.pubsub.model.json.Commerce
import io.glitchlib.pubsub.model.json.Following
import io.glitchlib.pubsub.model.json.GiftSubscriptionMessage
import io.glitchlib.pubsub.model.json.Host
import io.glitchlib.pubsub.model.json.MessageDelete
import io.glitchlib.pubsub.model.json.ModerationData
import io.glitchlib.pubsub.model.json.Moderator
import io.glitchlib.pubsub.model.json.StreamDown
import io.glitchlib.pubsub.model.json.StreamUp
import io.glitchlib.pubsub.model.json.SubscriptionMessage
import io.glitchlib.pubsub.model.json.Timeout
import io.glitchlib.pubsub.model.json.Unban
import io.glitchlib.pubsub.model.json.VideoPlayback
import io.glitchlib.pubsub.model.json.ViewCount
import io.glitchlib.pubsub.model.json.WhisperMessage
import io.glitchlib.pubsub.model.json.WhisperThread
import java.io.IOException
import java.util.UUID

class PubSubFormatter(private val client: GlitchClientImpl) : (String) -> IEvent {
    override fun invoke(raw: String): IEvent {
        val `object`: JsonObject = JsonParser().parse(raw).asJsonObject

        val type = MessageType.valueOf(`object`["type"].asString)

        return when (type) {
            MessageType.PING -> PingEvent(client)
            MessageType.PONG -> PongEvent(client)
            MessageType.RECONNECT -> ReconnectRequiredEvent(client)
            MessageType.RESPONSE -> doResponse(client, `object`)
            MessageType.MESSAGE -> doMessage(client, `object`)
            else -> PubSubEvent(client, `object`)
        }
    }


    private fun doResponse(client: GlitchClientImpl, data: JsonObject): IEvent {
        val nonce = UUID.fromString(data["nonce"].asString)
        val error = data["error"].asString.orEmpty()
        val topic = client.settings.topics.firstOrNull { it.code == nonce }

        return if (topic != null) {
            return if (error.isBlank()) {
                SuccessfulResponseEvent(client, topic)
            } else {
                val err = when (error) {
                    "ERR_BADMESSAGE" -> "Inappropriate message!"
                    "ERR_BADAUTH" -> "Failed to using authorization!"
                    "ERR_SERVER" -> "Internal Server Error!"
                    "ERR_BADTOPIC" -> "Inappropriate topic!"
                    else -> error
                }
                ErrorResponseEvent(client, topic, TopicException(err))
            }
        } else {
            ErrorResponseEvent(client, null, TopicException("Unknown registered topic for nonce: $nonce"))
        }
    }

    private fun doMessage(client: GlitchClientImpl, `object`: JsonObject): IEvent {
        val data = `object`["data"].asJsonObject
        val topicRaw = data.get("topic").asString
        val rawMessage = JsonParser().parse(data.get("message").asString).asJsonObject
        val topic = client.settings.topics.firstOrNull { it.rawType == topicRaw }

        return if (topic != null) handleMessage(client, topic, rawMessage) else UnknownMessageEvent(
            client,
            topicRaw,
            rawMessage
        )
    }

    private fun handleMessage(client: GlitchClientImpl, topic: Topic, rawMessage: JsonObject): IEvent {
        return when (topic.type) {
            TopicType.FOLLOW -> message_follow(client, topic, rawMessage)
            TopicType.WHISPERS -> message_whisper(client, topic, rawMessage)
            TopicType.CHANNEL_BITS, TopicType.CHANNEL_BITS_V2 -> message_bits(client, topic, rawMessage)
            TopicType.VIDEO_PLAYBACK -> message_playback(client, topic, rawMessage)
            TopicType.CHANNEL_COMMERCE -> message_commerce(client, topic, rawMessage)
            TopicType.CHANNEL_SUBSCRIPTION -> message_sub(client, topic, rawMessage)
            TopicType.CHAT_MODERATION_ACTIONS -> message_moderation(client, topic, rawMessage)
            TopicType.CHANNEL_EXTENSION_BROADCAST -> message_ebs(client, topic, rawMessage)
            TopicType.CHANNEL_BITS_BADGE_UNLOCK -> message_bits_badge_unlocked(client, topic, rawMessage)
        }
    }

    private fun message_bits_badge_unlocked(client: GlitchClientImpl, topic: Topic, rawMessage: JsonObject): IEvent =
        TODO("Not yet supported for this topic ${topic.rawType}")

    private fun message_follow(client: GlitchClientImpl, topic: Topic, rawMessage: JsonObject) =
        FollowEvent(client, topic, client.http.gson.fromJson(rawMessage, Following::class.java))

    private fun message_whisper(client: GlitchClientImpl, topic: Topic, rawMessage: JsonObject) =
        client.http.gson.fromJson(rawMessage, WhisperMode::class.java).let {
            when (it.type) {
                WhisperMode.Type.THREAD ->
                    WhisperThreadEvent(client, topic, client.http.gson.fromJson(it.data, WhisperThread::class.java))
                WhisperMode.Type.WHISPER_RECEIVED ->
                    WhisperReceivedEvent(client, topic, client.http.gson.fromJson(it.data, WhisperMessage::class.java))
                WhisperMode.Type.WHISPER_SENT ->
                    WhisperSentEvent(client, topic, client.http.gson.fromJson(it.data, WhisperMessage::class.java))
            }
        }

    private fun message_bits(client: GlitchClientImpl, topic: Topic, rawMessage: JsonObject) =
        BitsEvent(client, topic, client.http.gson.fromJson(rawMessage, BitsMessage::class.java))

    private fun message_playback(client: GlitchClientImpl, topic: Topic, rawMessage: JsonObject) =
        client.http.gson.fromJson(rawMessage, VideoPlayback::class.java).let {
            when (it.type) {
                VideoPlayback.Type.STREAM_UP ->
                    StreamUpEvent(client, topic, client.http.gson.fromJson(rawMessage, StreamUp::class.java))
                VideoPlayback.Type.STREAM_DOWN ->
                    StreamDownEvent(client, topic, client.http.gson.fromJson(rawMessage, StreamDown::class.java))
                VideoPlayback.Type.VIEW_COUNT ->
                    ViewCountEvent(client, topic, client.http.gson.fromJson(rawMessage, ViewCount::class.java))
            }
        }

    private fun message_commerce(client: GlitchClientImpl, topic: Topic, rawMessage: JsonObject) =
        CommerceEvent(client, topic, client.http.gson.fromJson(rawMessage, Commerce::class.java))

    private fun message_sub(client: GlitchClientImpl, topic: Topic, rawMessage: JsonObject) =
        client.http.gson.fromJson(rawMessage, SubscriptionMessage::class.java).let {
            if (it.context == SubscriptionContext.SUBGIFT) {
                SubGiftEvent(client, topic, client.http.gson.fromJson(rawMessage, GiftSubscriptionMessage::class.java))
            } else {
                SubscriptionEvent(client, topic, it)
            }
        }

    private fun message_moderation(
        client: GlitchClientImpl,
        topic: Topic,
        rawMessage: JsonObject
    ) = client.http.gson.fromJson(rawMessage, ModerationData::class.java).let {
        when (it.moderationAction) {
            ModerationData.Action.DELETE ->
                MessageDeleteEvent(client, topic, MessageDelete(it))
            ModerationData.Action.TIMEOUT ->
                TimeoutUserEvent(client, topic, Timeout(it))
            ModerationData.Action.BAN ->
                BanUserEvent(client, topic, Ban(it))
            ModerationData.Action.UNBAN, ModerationData.Action.UNTIMEOUT ->
                UnbanUserEvent(client, topic, Unban(it))
            ModerationData.Action.HOST ->
                HostEvent(client, topic, Host(it))
            ModerationData.Action.SUBSCRIBERS, ModerationData.Action.SUBSCRIBERSOFF ->
                SubscribersOnlyEvent(
                    client,
                    topic,
                    ActivationByMod(it, it.moderationAction == ModerationData.Action.SUBSCRIBERS)
                )
            ModerationData.Action.CLEAR ->
                ClearChatEvent(client, topic, Moderator(it))
            ModerationData.Action.EMOTEONLY, ModerationData.Action.EMOTEONLYOFF ->
                EmoteOnlyEvent(
                    client,
                    topic,
                    ActivationByMod(it, it.moderationAction == ModerationData.Action.EMOTEONLY)
                )
            ModerationData.Action.R9KBETA, ModerationData.Action.R9KBETAOFF ->
                Robot9000Event(
                    client,
                    topic,
                    ActivationByMod(it, it.moderationAction == ModerationData.Action.R9KBETA)
                )
        }
    }

    private fun message_ebs(client: GlitchClientImpl, topic: Topic, rawMessage: JsonObject) =
        ChannelExtensionBroadcastEvent(
            client,
            topic,
            client.http.gson.toJsonTree(rawMessage).asJsonObject.getAsJsonArray("content")
        )


    internal data class WhisperMode(
        @JsonAdapter(WhisperTypeAdapter::class)
        internal val type: Type,
        internal val data: String
    ) {
        internal enum class Type {
            WHISPER_RECEIVED,
            WHISPER_SENT,
            THREAD
        }

        internal inner class WhisperTypeAdapter : TypeAdapter<Type>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: Type) {
                out.value(value.name.toLowerCase())
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader): Type {
                for (t in Type.values()) {
                    if (t.name.equals(`in`.nextString(), ignoreCase = true)) {
                        return t
                    }
                }
                throw JsonParseException("Unknown whisper type: " + `in`.nextString())
            }
        }
    }

}
