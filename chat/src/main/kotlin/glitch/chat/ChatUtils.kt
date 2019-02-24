package glitch.chat

import glitch.api.ws.events.IEvent
import glitch.api.ws.events.PingEvent
import glitch.api.ws.events.PongEvent
import glitch.chat.events.*
import glitch.chat.irc.Command
import glitch.chat.events.IRCEvent
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
internal object ChatUtils {
    @JvmStatic
    fun consume(event: IRCEvent) {
        when (event.command) {
            Command.JOIN -> doUserJoinChannel(event)
            Command.PART -> doUserLeavingChannel(event)
            Command.PING -> doPing(event)
            Command.PONG -> doPong(event)
            Command.NOTICE -> doNotice(event)
            Command.PRIV_MSG -> doChannelMessage(event)
            Command.WHISPER -> doWhisper(event)
            Command.USER_NOTICE -> doUserNotice(event)
            Command.ROOM_STATE -> doChannelState(event)
            Command.GLOBAL_USER_STATE -> doGlobalUserState(event)
            Command.USER_STATE -> doChannelUserState(event)
            Command.CLEAR_CHAT -> doClearChat(event)
            Command.HOST_TARGET -> doHost(event)
            Command.CLEAR_MESSAGE -> doChannelDeletedMessage(event)
        }
    }

    private fun doUserJoinChannel(event: IRCEvent) = event.dispatch(UserJoinedChannelEvent(event.client, event.middle[0].substring(1), event.prefix.user!!))
    private fun doUserLeavingChannel(event: IRCEvent) = event.dispatch(UserLeavingChannelEvent(event.client, event.middle[0].substring(1), event.prefix.user!!))
    private fun doPing(event: IRCEvent) = event.dispatch(PingEvent(event.client))
    private fun doPong(event: IRCEvent) = event.dispatch(PongEvent(event.client))
    private fun doNotice(event: IRCEvent) {

    }

    private fun doChannelMessage(event: IRCEvent) {
        val username = event.prefix.nick!!
        val channelId = event.tags.getLong("room-id")
        val channelName = event.middle[0].substring(1)

        if (username == "jtv") {
            event.dispatch(event.trailing!!.let {
                val host = it.split(' ').first()
                val views = it.split("up to ")[1]
                        .let { if (it.contains(' ')) it.split(' ')[0] else it }
                        .trim().toInt()
                val autoHost = it.contains("auto hosting")

                return@let ChannelHostedEvent(event.client, channelName,
                        channelId, host, views, autoHost)
            })
        } else {

            val bits = event.tags.getInteger("bits")
            val displayName = event.tags["display-name"]!!
            val id = UUID.fromString(event.tags["id"]!!)
            val timestamp = event.tags.sentTimestamp
            val userId = event.tags.getLong("user-id")

            event.dispatch(
                    when {
                        bits > 0 -> ChannelBitsMessageEvent(event.client, id, channelId, userId, event.tags.badges,
                                event.tags.color!!, event.tags.userType, displayName, channelName, username,
                                event.formattedTrailing, timestamp, bits, event.isActionMessage)
                        else -> ChannelMessageEvent(event.client, id, channelId, userId, event.tags.badges, event.tags.color!!,
                                event.tags.userType, displayName, channelName, username, event.formattedTrailing,
                                timestamp, event.isActionMessage)
                    }
            )
        }
    }

    private fun doWhisper(event: IRCEvent) {
        val username = event.prefix.nick!!
        val displayName = event.tags["display-name"]!!
        val timestamp = event.tags.sentTimestamp
        val id = event.tags.getInteger("id")
        val userId = event.tags.getLong("user-id")

        event.client.ws.dispatch(PrivateMessageEvent(
                event.client,
                id,
                event.tags.badges,
                event.tags.color!!,
                username,
                displayName,
                userId,
                event.tags.userType,
                event.trailing,
                timestamp
        ))
    }

    private fun doUserNotice(event: IRCEvent) {
        when (event.tags["msg-id"]) {
            "sub" -> doSub(event)
            "resub" -> doResub(event)
            "subgift" -> doSubGift(event)
            "anonsubgift" -> doAnonSubGift(event)
            "raid" -> doRaid(event)
            "ritual" -> doRitual(event)
        }
    }

    private fun doChannelState(event: IRCEvent) {
        val channelId = event.tags.getLong("room-id")

        if (event.tags.size > 2) {
            val loc = event.tags.broadcasterLanguage
            val emotes = event.tags.getBoolean("emote-only")
            val follows = event.tags.getInteger("followers-only")
            val r9k = event.tags.getBoolean("r9k")
            val slow = event.tags.getInteger("slow")
            val sub = event.tags.getBoolean("subs-only")

            event.client.ws.dispatch(ChannelStateEvent(
                    event.client, event.middle[0].substring(1),
                    channelId, loc, emotes, follows, r9k, slow, sub
            ))
        } else {
            event.client.ws.dispatch(ChannelStateChangedEvent(
                    event.client, event.middle[0].substring(1),
                    channelId, event.tags.keys.first(), event.tags.values.firstOrNull()
            ))
        }
    }

    private fun doGlobalUserState(event: IRCEvent) {}
    private fun doChannelUserState(event: IRCEvent) {}
    private fun doClearChat(event: IRCEvent) {}
    private fun doHost(event: IRCEvent) {}
    private fun doChannelDeletedMessage(event: IRCEvent) {
        event.client.ws.dispatch(ChannelMessageDeletedEvent(
                event.client,
                UUID.fromString(event.tags["target-msg-id"]),
                event.middle[0].substring(1),
                event.tags.getLong("user-id"),
                event.tags["login"]!!,
                event.formattedTrailing,
                event.isActionMessage
        ))
    }

    // USERNOTICE
    private fun doSub(event: IRCEvent) {}

    private fun doResub(event: IRCEvent) {}
    private fun doSubGift(event: IRCEvent) {}
    private fun doAnonSubGift(event: IRCEvent) {}
    private fun doRaid(event: IRCEvent) {}
    private fun doRitual(event: IRCEvent) {}


    private fun IRCEvent.dispatch(event: IEvent<GlitchChat>) = client.ws.dispatch(event)
}