package io.glitchlib.tmi.internal

import io.glitchlib.tmi.Channel
import io.glitchlib.tmi.ChannelUser
import io.glitchlib.tmi.ChatRoom
import io.glitchlib.tmi.ChatRoomUser
import io.glitchlib.tmi.MessageInterface
import io.glitchlib.tmi.event.ChannelStateChangedEvent
import io.glitchlib.tmi.event.ChannelStateEvent
import io.glitchlib.tmi.event.ChannelUserState
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.util.UUID

class ChatRoomImpl(
    private val tmi: MessageInterface,
    override val roomData: io.glitchlib.model.ChatRoom
) : ChatRoom {
    override var isEmoteOnly: Boolean = false
        private set
    override var isR9k: Boolean = false
        private set
    override var slow: Int = 0
        private set
    override val assignableChannel: Maybe<Channel>
        get() = Flowable.fromIterable(tmi._channels).filter {
            it.rooms.map { it.id == roomData.id }.filter { it }.blockingFirst()
        }.firstElement()
    override val id: UUID
        get() = roomData.id
    override val users: Flowable<ChatRoomUser>
        get() = Flowable.fromIterable(_users.map { Pair(it.key, it.value) })
            .map { ChatRoomUserImpl(this, it.first, it.second) }
    override val name: String
        get() = roomData.name
    override val vips: Flowable<ChatRoomUser>
        get() = users.filter { it.isVip }
    override val subscribers: Flowable<ChatRoomUser>
        get() = users.filter { it.isSubscriber }
    override val mods: Flowable<ChatRoomUser>
        get() = users.filter { it.isMod }


    internal val _users: MutableMap<String, ChannelUserState> = mutableMapOf()

    init {
        tmi.onEvent<ChannelStateEvent>().subscribe {
            if (it.channelName == "chatrooms:$slug") {
                isEmoteOnly = it.isEmoteOnly
                isR9k = it.isR9k
                slow = it.slow
            }
        }

        tmi.onEvent<ChannelStateChangedEvent>().subscribe {
            if (it.channelName == "chatrooms:$slug") {
                when (it.key) {
                    "emote-only" -> isEmoteOnly = it.value?.toInt()!! > 0
                    "r9k" -> isR9k = it.value?.toInt()!! > 0
                    "slow" -> slow = it.value?.toInt()!!
                }
            }
        }
    }

    override fun send(message: String): Completable =
        tmi.sendChannelMsg("${roomData.ownerId}:${roomData.id}", message)

    override fun leave(): Completable = tmi.sendPart(roomData)
}