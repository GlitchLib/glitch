package io.glitchlib.tmi.internal

import io.glitchlib.auth.GlobalUserState
import io.glitchlib.tmi.Channel
import io.glitchlib.tmi.ChannelUser
import io.glitchlib.tmi.MessageInterface
import io.glitchlib.tmi.event.ChannelStateChangedEvent
import io.glitchlib.tmi.event.ChannelStateEvent
import io.glitchlib.tmi.event.ChannelUserState
import io.reactivex.Completable
import io.reactivex.Flowable

class ChannelImpl(
    val tmi: MessageInterface,
    private val globalUserState: GlobalUserState
) : Channel {
    override var isEmoteOnly: Boolean = false
        private set
    override var follows: Int = -1
        private set
    override var isR9k: Boolean = false
        private set
    override var slow: Int = 0
        private set
    override var isSubOnly: Boolean = false
        private set

    init {
        tmi.onEvent<ChannelStateEvent>().subscribe {
            if (it.channelName == globalUserState.login) {
                isEmoteOnly = it.isEmoteOnly
                follows = it.follows
                isR9k = it.isR9k
                slow = it.slow
                isSubOnly = it.isSubOnly
            }
        }

        tmi.onEvent<ChannelStateChangedEvent>().subscribe {
            if (it.channelName == globalUserState.login) {
                when (it.key) {
                    "emote-only" -> isEmoteOnly = it.value?.toInt()!! > 0
                    "followers-only" -> follows = it.value?.toInt()!!
                    "r9k" -> isR9k = it.value?.toInt()!! > 0
                    "slow" -> slow = it.value?.toInt()!!
                    "subs-only" -> isSubOnly = it.value?.toInt()!! > 0
                }
            }
        }
    }

    override val name: String
        get() = globalUserState.login
    override val rooms: Flowable<io.glitchlib.model.ChatRoom>
        get() = tmi.client.getChatRooms(globalUserState.id)

    override val users: Flowable<ChannelUser>
        get() = Flowable.fromIterable(_users.map { Pair(it.key, it.value) })
            .map { ChannelUserImpl(this, it.first, it.second) }

    internal val _users: MutableMap<String, ChannelUserState> = mutableMapOf()

    override fun leave(): Completable = tmi.sendPart(name)

    override fun send(message: String): Completable =
        tmi.sendPrivMsg(globalUserState.login, message)
}