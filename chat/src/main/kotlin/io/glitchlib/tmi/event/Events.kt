package io.glitchlib.tmi.event

import io.glitchlib.model.IEvent
import io.glitchlib.model.PingEvent
import io.glitchlib.model.PongEvent
import io.glitchlib.tmi.irc.Command
import java.util.UUID

object Events {
    fun from(event: IRCEvent, consumer: (IEvent) -> Unit) {
        when (event.command) {
            Command.JOIN -> doUserJoinChannel(event, consumer)
            Command.PART -> doUserLeavingChannel(event, consumer)
            Command.PING -> doPing(event, consumer)
            Command.PONG -> doPong(event, consumer)
            Command.NOTICE -> doNotice(event, consumer)
            Command.PRIV_MSG -> doChannelMessage(event, consumer)
            Command.WHISPER -> doWhisper(event, consumer)
            Command.USER_NOTICE -> doUserNotice(event, consumer)
            Command.ROOM_STATE -> doChannelState(event, consumer)
            Command.GLOBAL_USER_STATE -> doGlobalUserState(event, consumer)
            Command.USER_STATE -> doChannelUserState(event, consumer)
            Command.CLEAR_CHAT -> doClearChat(event, consumer)
            Command.HOST_TARGET -> doHost(event, consumer)
            Command.CLEAR_MESSAGE -> doChannelDeletedMessage(event, consumer)
        }
    }

    private fun doUserJoinChannel(event: IRCEvent, consumer: (IEvent) -> Unit) =
        consumer(UserJoinedChannelEvent(event.client, event.middle[0].substring(1), event.prefix.user!!))

    private fun doUserLeavingChannel(event: IRCEvent, consumer: (IEvent) -> Unit) =
        consumer(UserLeavingChannelEvent(event.client, event.middle[0].substring(1), event.prefix.user!!))

    private fun doPing(event: IRCEvent, consumer: (IEvent) -> Unit) = consumer(PingEvent(event.client))
    private fun doPong(event: IRCEvent, consumer: (IEvent) -> Unit) = consumer(PongEvent(event.client))
    private fun doNotice(event: IRCEvent, consumer: (IEvent) -> Unit) {

    }

    private fun doChannelMessage(event: IRCEvent, consumer: (IEvent) -> Unit) {
        val username = event.prefix.nick!!
        val channelId = event.tags.getLong("room-id")
        val channelName = event.middle[0].substring(1)

        if (username == "jtv") {
            consumer(event.trailing!!.let {
                val host = it.split(' ').first()
                val views = it.split("up to ")[1]
                    .let { if (it.contains(' ')) it.split(' ')[0] else it }
                    .trim().toInt()
                val autoHost = it.contains("auto hosting")

                return@let ChannelHostedEvent(
                    event.client, channelName,
                    channelId, host, views, autoHost
                )
            })
        } else {

            val bits = event.tags.getInteger("bits")
            val displayName = event.tags["display-name"]!!
            val id = UUID.fromString(event.tags["id"]!!)
            val timestamp = event.tags.sentTimestamp
            val userId = event.tags.getLong("user-id")

            consumer(
                when {
                    bits > 0 -> ChannelBitsMessageEvent(
                        event.client, id, channelId, userId, event.tags.badges,
                        event.tags.color!!, event.tags.userType, displayName, channelName, username,
                        event.formattedTrailing, timestamp, bits, event.isActionMessage
                    )
                    else -> ChannelMessageEvent(
                        event.client, id, channelId, userId, event.tags.badges, event.tags.color!!,
                        event.tags.userType, displayName, channelName, username, event.formattedTrailing,
                        timestamp, event.isActionMessage
                    )
                }
            )
        }
    }

    private fun doWhisper(event: IRCEvent, consumer: (IEvent) -> Unit) {
        val username = event.prefix.nick!!
        val displayName = event.tags["display-name"]!!
        val timestamp = event.tags.sentTimestamp
        val id = event.tags.getInteger("id")
        val userId = event.tags.getLong("user-id")

        consumer(
            PrivateMessageEvent(
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
            )
        )
    }

    private fun doUserNotice(event: IRCEvent, consumer: (IEvent) -> Unit) {
        when (event.tags["msg-id"]) {
            "sub" -> doSub(event, consumer)
            "resub" -> doResub(event, consumer)
            "subgift" -> doSubGift(event, consumer)
            "anonsubgift" -> doAnonSubGift(event, consumer)
            "raid" -> doRaid(event, consumer)
            "ritual" -> doRitual(event, consumer)
        }
    }

    private fun doChannelState(event: IRCEvent, consumer: (IEvent) -> Unit) {
        val channelId = event.tags.getLong("room-id")

        if (event.tags.size > 2) {
            val loc = event.tags.broadcasterLanguage
            val emotes = event.tags.getBoolean("emote-only")
            val follows = event.tags.getInteger("followers-only")
            val r9k = event.tags.getBoolean("r9k")
            val slow = event.tags.getInteger("slow")
            val sub = event.tags.getBoolean("subs-only")

            consumer(
                ChannelStateEvent(
                    event.client, event.middle[0].substring(1),
                    channelId, loc, emotes, follows, r9k, slow, sub
                )
            )
        } else {
            consumer(
                ChannelStateChangedEvent(
                    event.client, event.middle[0].substring(1),
                    channelId, event.tags.keys.first(), event.tags.values.firstOrNull()
                )
            )
        }
    }

    private fun doGlobalUserState(event: IRCEvent, consumer: (IEvent) -> Unit) {}
    private fun doChannelUserState(event: IRCEvent, consumer: (IEvent) -> Unit) {}
    private fun doClearChat(event: IRCEvent, consumer: (IEvent) -> Unit) {}
    private fun doHost(event: IRCEvent, consumer: (IEvent) -> Unit) {}
    private fun doChannelDeletedMessage(event: IRCEvent, consumer: (IEvent) -> Unit) {
        consumer(
            ChannelMessageDeletedEvent(
                event.client,
                UUID.fromString(event.tags["target-msg-id"]),
                event.middle[0].substring(1),
                event.tags.getLong("user-id"),
                event.tags["login"]!!,
                event.formattedTrailing,
                event.isActionMessage
            )
        )
    }

    // USERNOTICE
    private fun doSub(event: IRCEvent, consumer: (IEvent) -> Unit) {}

    private fun doResub(event: IRCEvent, consumer: (IEvent) -> Unit) {}
    private fun doSubGift(event: IRCEvent, consumer: (IEvent) -> Unit) {}
    private fun doAnonSubGift(event: IRCEvent, consumer: (IEvent) -> Unit) {}
    private fun doRaid(event: IRCEvent, consumer: (IEvent) -> Unit) {}
    private fun doRitual(event: IRCEvent, consumer: (IEvent) -> Unit) {}
}
