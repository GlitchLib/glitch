package glitch.pubsub

import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
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
object PubSubUtils {
    @JvmStatic
    fun consume(event: PubSubEvent) {
        val type = MessageType.valueOf(event.data.get("type").asString)

        when (type) {
            MessageType.PING -> event.dispatch(PingEvent(event.client))
            MessageType.PONG -> event.dispatch(PongEvent(event.client))
            MessageType.RECONNECT -> event.dispatch(ReconnectRequiredEvent(event.client))
            MessageType.RESPONSE -> doResponse(event)
            MessageType.MESSAGE -> doMessage(event)
        }
    }

    private fun doResponse(event: PubSubEvent) =
            event.dispatch(event.let {
                val topicsCache = it.client.topicsCache
                val nonce = UUID.fromString(it.data["nonce"].asString)
                val error = it.data["error"].asString.orEmpty()
                for (topic in topicsCache.all) {
                    if (topic.code == nonce) {
                        return@let if (error.isBlank()) {
                            SucessfulResponseEvent(event.client, topic)
                        } else {
                            val err = when (error) {
                                "ERR_BADMESSAGE" -> "Inappropriate message!"
                                "ERR_BADAUTH" -> "Failed to using authorization!"
                                "ERR_SERVER" -> "Internal Server Error!"
                                "ERR_BADTOPIC" -> "Inappropriate topic!"
                                else -> error
                            }
                            ErrorResponseEvent(event.client, topic, TopicException(err))
                        }
                    }
                }
                return@let ErrorResponseEvent(event.client, null, TopicException("Unknown registered topic for nonce: $nonce"))
            })

    private fun doMessage(event: PubSubEvent) {
        val topicsCache = event.client.topicsCache
        val data = event.data.get("data").asJsonObject
        val topicRaw = data.get("topic").asString
        val rawMessage = data.get("message").asString

        for (topic in topicsCache.active) {
            if (topic.rawType == topicRaw) {
                handleMessage(event, topic, rawMessage)
                return
            }
        }

        event.dispatch(UnknownMessageEvent(event.client, topicRaw, event.mapper.toJsonTree(rawMessage).asJsonObject))
    }

    private fun PubSubEvent.dispatch(event: IEvent<GlitchPubSub>) = client.ws.dispatch(event)

    private fun handleMessage(event: PubSubEvent, topic: Topic, rawMessage: String) {
        val client = event.client

        when (topic.type) {
            Topic.Type.FOLLOW -> message_follow(event, topic, rawMessage)
            Topic.Type.WHISPERS -> message_whisper(event, topic, rawMessage)
            Topic.Type.CHANNEL_BITS, Topic.Type.CHANNEL_BITS_V2 -> message_bits(event, topic, rawMessage)
            Topic.Type.VIDEO_PLAYBACK -> message_playback(event, topic, rawMessage)
            Topic.Type.CHANNEL_COMMERCE -> message_commerce(event, topic, rawMessage)
            Topic.Type.CHANNEL_SUBSCRIPTION -> message_sub(event, topic, rawMessage)
            Topic.Type.CHAT_MODERATION_ACTIONS -> message_moderation(event, topic, rawMessage)
            Topic.Type.CHANNEL_EXTENSION_BROADCAST -> message_ebs(event, topic, rawMessage)
        }
    }

    private fun message_follow(event: PubSubEvent, topic: Topic, rawMessage: String) {
        event.dispatch(FollowEvent(event.client, topic, event.mapper.fromJson(rawMessage, Following::class.java)))
    }

    private fun message_whisper(event: PubSubEvent, topic: Topic, rawMessage: String) {
        val whisper = event.mapper.fromJson(rawMessage, WhisperMode::class.java)
        when (whisper.type) {
            PubSubUtils.WhisperMode.Type.THREAD ->
                event.dispatch(WhisperThreadEvent(event.client, topic, event.mapper.fromJson(whisper.data, WhisperThread::class.java)))
            PubSubUtils.WhisperMode.Type.WHISPER_RECEIVED ->
                event.dispatch(WhisperReceivedEvent(event.client, topic, event.mapper.fromJson(whisper.data, WhisperMessage::class.java)))
            PubSubUtils.WhisperMode.Type.WHISPER_SENT ->
                event.dispatch(WhisperSentEvent(event.client, topic, event.mapper.fromJson(whisper.data, WhisperMessage::class.java)))
        }
    }

    private fun message_bits(event: PubSubEvent, topic: Topic, rawMessage: String) {
        event.dispatch(BitsEvent(event.client, topic, event.mapper.fromJson(rawMessage, BitsMessage::class.java)))
    }

    private fun message_playback(event: PubSubEvent, topic: Topic, rawMessage: String) {
        val playback = event.mapper.fromJson(rawMessage, VideoPlayback::class.java)

        when (playback.type) {
            VideoPlayback.Type.STREAM_UP ->
                event.dispatch(StreamUpEvent(event.client, topic, event.mapper.fromJson(rawMessage, StreamUp::class.java)))
            VideoPlayback.Type.STREAM_DOWN ->
                event.dispatch(StreamDownEvent(event.client, topic, event.mapper.fromJson(rawMessage, StreamDown::class.java)))
            VideoPlayback.Type.VIEW_COUNT ->
                event.dispatch(ViewCountEvent(event.client, topic, event.mapper.fromJson(rawMessage, ViewCount::class.java)))
        }
    }

    private fun message_commerce(event: PubSubEvent, topic: Topic, rawMessage: String) {
        event.dispatch(CommerceEvent(event.client, topic, event.mapper.fromJson(rawMessage, Commerce::class.java)))
    }

    private fun message_sub(event: PubSubEvent, topic: Topic, rawMessage: String) {
        val sub = event.mapper.fromJson(rawMessage, SubscriptionMessage::class.java)

        if (sub.context == SubscriptionContext.SUBGIFT) {
            event.dispatch(SubGiftEvent(event.client, topic, event.mapper.fromJson(rawMessage, GiftSubscriptionMessage::class.java)))
        } else {
            event.dispatch(SubscriptionEvent(event.client, topic, sub))
        }
    }

    private fun message_moderation(event: PubSubEvent, topic: Topic, rawMessage: String) {
        val modData = event.mapper.fromJson(rawMessage, ModerationData::class.java)

        when (modData.moderationAction) {
            ModerationData.Action.DELETE ->
                event.dispatch(MessageDeleteEvent(event.client, topic, MessageDelete(modData)))
            ModerationData.Action.TIMEOUT ->
                event.dispatch(TimeoutUserEvent(event.client, topic, Timeout(modData)))
            ModerationData.Action.BAN ->
                event.dispatch(BanUserEvent(event.client, topic, Ban(modData)))
            ModerationData.Action.UNBAN, ModerationData.Action.UNTIMEOUT ->
                event.dispatch(UnbanUserEvent(event.client, topic, Unban(modData)))
            ModerationData.Action.HOST ->
                event.dispatch(HostEvent(event.client, topic, Host(modData)))
            ModerationData.Action.SUBSCRIBERS, ModerationData.Action.SUBSCRIBERSOFF ->
                event.dispatch(SubscribersOnlyEvent(event.client, topic, ActivationByMod(modData, modData.moderationAction == ModerationData.Action.SUBSCRIBERS)))
            ModerationData.Action.CLEAR ->
                event.dispatch(ClearChatEvent(event.client, topic, Moderator(modData)))
            ModerationData.Action.EMOTEONLY, ModerationData.Action.EMOTEONLYOFF ->
                event.dispatch(EmoteOnlyEvent(event.client, topic, ActivationByMod(modData, modData.moderationAction == ModerationData.Action.EMOTEONLY)))
            ModerationData.Action.R9KBETA, ModerationData.Action.R9KBETAOFF ->
                event.dispatch(Robot9000Event(event.client, topic, ActivationByMod(modData, modData.moderationAction == ModerationData.Action.R9KBETA)))

        }
    }

    private fun message_ebs(event: PubSubEvent, topic: Topic, rawMessage: String) {
        event.dispatch(ChannelExtensionBroadcastEvent(event.client, topic, event.mapper.toJsonTree(rawMessage).asJsonObject.getAsJsonArray("content")))
    }

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