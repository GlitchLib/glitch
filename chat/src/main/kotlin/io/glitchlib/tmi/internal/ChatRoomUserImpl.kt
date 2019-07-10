package io.glitchlib.tmi.internal

import io.glitchlib.tmi.ChatRoom
import io.glitchlib.tmi.ChatRoomUser
import io.glitchlib.tmi.event.ChannelUserState
import io.reactivex.Completable

class ChatRoomUserImpl(
    val chatRoom: ChatRoom,
    override val username: String,
    userState: ChannelUserState
) : ChatRoomUser, ChannelUserState by userState {

    override fun mod(): Completable =
        chatRoom.send("/${if (isMod) "un" else ""}mod $username")

    override fun ban(reason: String?): Completable =
        chatRoom.send("/ban $username${if (reason != null) " $reason" else ""}")

    override fun timeout(seconds: Long, reason: String?): Completable =
        chatRoom.send("/timeout $username $seconds${if (reason != null) " $reason" else ""}")

    override fun sendDM(message: String): Completable =
        chatRoom.send("/w $username $message")

}
