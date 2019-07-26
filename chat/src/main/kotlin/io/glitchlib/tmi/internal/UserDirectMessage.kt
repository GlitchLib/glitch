package io.glitchlib.tmi.internal

import io.glitchlib.auth.GlobalUserState
import io.glitchlib.tmi.Channel
import io.glitchlib.tmi.ChannelUser
import io.glitchlib.tmi.ChatRoom
import io.glitchlib.tmi.ChatRoomUser
import io.glitchlib.tmi.MessageInterface
import io.glitchlib.tmi.UserDM
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.util.UUID

class UserDirectMessage(
    private val tmi: MessageInterface,
    override val globalUserState: GlobalUserState
) : UserDM {
    override val assignableChannels: Flowable<Channel>
        get() = Flowable.fromIterable(tmi.channels.filter { (it as ChannelImpl)._users.containsKey(globalUserState.login) })
    override val assignableRooms: Flowable<ChatRoom>
        get() = Flowable.fromIterable(tmi.chatRooms.filter { (it as ChatRoomImpl)._users.containsKey(globalUserState.login) })

    override fun send(message: String): Completable =
        tmi.sendDM(globalUserState.login, message)

    override fun getFromChannel(channel: String): Maybe<ChannelUser> = assignableChannels.filter { it.name == channel }
        .firstElement().flatMap { it.getUser(globalUserState.login) }

    override fun getFromChatRoom(id: UUID): Maybe<ChatRoomUser> =
        assignableRooms.filter { it.id == id }.firstElement()
            .flatMap { it.getUser(globalUserState.login) }
}