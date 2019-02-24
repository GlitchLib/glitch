package glitch.pubsub

import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import glitch.api.ws.IEventConverter
import glitch.api.ws.events.IEvent
import glitch.api.ws.events.PingEvent
import glitch.api.ws.events.PongEvent
import glitch.pubsub.`object`.enums.MessageType
import glitch.pubsub.`object`.enums.SubscriptionContext
import glitch.pubsub.events.*
import glitch.pubsub.events.json.*
import glitch.pubsub.exceptions.TopicException
import java.io.IOException
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class PubSubConverter(private val gson: Gson) : IEventConverter<GlitchPubSub> {


    override fun convert(client: GlitchPubSub, raw: String): IEvent<GlitchPubSub> {
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

    private fun doResponse(client: GlitchPubSub, data: JsonObject): IEvent<GlitchPubSub> {
        val topicsCache = client.topicsCache
        val nonce = UUID.fromString(data["nonce"].asString)
        val error = data["error"].asString.orEmpty()
        for (topic in topicsCache.active) {
            if (topic.code == nonce) {
                return if (error.isBlank()) {
                    SucessfulResponseEvent(client, topic)
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
            }
        }
        return ErrorResponseEvent(client, null, TopicException("Unknown registered topic for nonce: $nonce"))
    }

    private fun doMessage(client: GlitchPubSub, `object`: JsonObject): IEvent<GlitchPubSub> {
        val topicsCache = client.topicsCache
        val data = `object`["data"].asJsonObject
        val topicRaw = data.get("topic").asString
        val rawMessage = JsonParser().parse(data.get("message").asString).asJsonObject

        for (topic in topicsCache.active) {
            if (topic.rawType == topicRaw) {
                return handleMessage(client, topic, rawMessage)
            }
        }

        return UnknownMessageEvent(client, topicRaw, rawMessage)
    }

    private fun handleMessage(client: GlitchPubSub, topic: Topic, rawMessage: JsonObject): IEvent<GlitchPubSub> {
        return when (topic.type) {
            Topic.Type.FOLLOW -> message_follow(client, topic, rawMessage)
            Topic.Type.WHISPERS -> message_whisper(client, topic, rawMessage)
            Topic.Type.CHANNEL_BITS, Topic.Type.CHANNEL_BITS_V2 -> message_bits(client, topic, rawMessage)
            Topic.Type.VIDEO_PLAYBACK -> message_playback(client, topic, rawMessage)
            Topic.Type.CHANNEL_COMMERCE -> message_commerce(client, topic, rawMessage)
            Topic.Type.CHANNEL_SUBSCRIPTION -> message_sub(client, topic, rawMessage)
            Topic.Type.CHAT_MODERATION_ACTIONS -> message_moderation(client, topic, rawMessage)
            Topic.Type.CHANNEL_EXTENSION_BROADCAST -> message_ebs(client, topic, rawMessage)
        }
    }

    private fun message_follow(client: GlitchPubSub, topic: Topic, rawMessage: JsonObject) =
            FollowEvent(client, topic, gson.fromJson(rawMessage, Following::class.java))

    private fun message_whisper(client: GlitchPubSub, topic: Topic, rawMessage: JsonObject): IEvent<GlitchPubSub> {
        val whisper = gson.fromJson(rawMessage, WhisperMode::class.java)
        return when (whisper.type) {
            WhisperMode.Type.THREAD ->
                WhisperThreadEvent(client, topic, gson.fromJson(whisper.data, WhisperThread::class.java))
            WhisperMode.Type.WHISPER_RECEIVED ->
                WhisperReceivedEvent(client, topic, gson.fromJson(whisper.data, WhisperMessage::class.java))
            WhisperMode.Type.WHISPER_SENT ->
                WhisperSentEvent(client, topic, gson.fromJson(whisper.data, WhisperMessage::class.java))
        }
    }

    private fun message_bits(client: GlitchPubSub, topic: Topic, rawMessage: JsonObject) =
            BitsEvent(client, topic, gson.fromJson(rawMessage, BitsMessage::class.java))

    private fun message_playback(client: GlitchPubSub, topic: Topic, rawMessage: JsonObject): IEvent<GlitchPubSub> {
        val playback = gson.fromJson(rawMessage, VideoPlayback::class.java)

        return when (playback.type) {
            VideoPlayback.Type.STREAM_UP ->
                StreamUpEvent(client, topic, gson.fromJson(rawMessage, StreamUp::class.java))
            VideoPlayback.Type.STREAM_DOWN ->
                StreamDownEvent(client, topic, gson.fromJson(rawMessage, StreamDown::class.java))
            VideoPlayback.Type.VIEW_COUNT ->
                ViewCountEvent(client, topic, gson.fromJson(rawMessage, ViewCount::class.java))
        }
    }

    private fun message_commerce(client: GlitchPubSub, topic: Topic, rawMessage: JsonObject) =
            CommerceEvent(client, topic, gson.fromJson(rawMessage, Commerce::class.java))

    private fun message_sub(client: GlitchPubSub, topic: Topic, rawMessage: JsonObject): IEvent<GlitchPubSub> {
        val sub = gson.fromJson(rawMessage, SubscriptionMessage::class.java)

        return if (sub.context == SubscriptionContext.SUBGIFT) {
            SubGiftEvent(client, topic, gson.fromJson(rawMessage, GiftSubscriptionMessage::class.java))
        } else {
            SubscriptionEvent(client, topic, sub)
        }
    }

    private fun message_moderation(client: GlitchPubSub, topic: Topic, rawMessage: JsonObject): IEvent<GlitchPubSub> {
        val modData = gson.fromJson(rawMessage, ModerationData::class.java)

        return when (modData.moderationAction) {
            ModerationData.Action.DELETE ->
                MessageDeleteEvent(client, topic, MessageDelete(modData))
            ModerationData.Action.TIMEOUT ->
                TimeoutUserEvent(client, topic, Timeout(modData))
            ModerationData.Action.BAN ->
                BanUserEvent(client, topic, Ban(modData))
            ModerationData.Action.UNBAN, ModerationData.Action.UNTIMEOUT ->
                UnbanUserEvent(client, topic, Unban(modData))
            ModerationData.Action.HOST ->
                HostEvent(client, topic, Host(modData))
            ModerationData.Action.SUBSCRIBERS, ModerationData.Action.SUBSCRIBERSOFF ->
                SubscribersOnlyEvent(client, topic, ActivationByMod(modData, modData.moderationAction == ModerationData.Action.SUBSCRIBERS))
            ModerationData.Action.CLEAR ->
                ClearChatEvent(client, topic, Moderator(modData))
            ModerationData.Action.EMOTEONLY, ModerationData.Action.EMOTEONLYOFF ->
                EmoteOnlyEvent(client, topic, ActivationByMod(modData, modData.moderationAction == ModerationData.Action.EMOTEONLY))
            ModerationData.Action.R9KBETA, ModerationData.Action.R9KBETAOFF ->
                Robot9000Event(client, topic, ActivationByMod(modData, modData.moderationAction == ModerationData.Action.R9KBETA))
        }
    }

    private fun message_ebs(client: GlitchPubSub, topic: Topic, rawMessage: JsonObject) =
            ChannelExtensionBroadcastEvent(client, topic, gson.toJsonTree(rawMessage).asJsonObject.getAsJsonArray("content"))


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