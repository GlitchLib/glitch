package io.glitchlib.tmi

import io.glitchlib.auth.GlobalUserState
import io.glitchlib.tmi.event.ChannelUserState
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import okhttp3.internal.toHexString
import java.awt.Color
import java.time.Duration
import java.util.UUID

interface UserDM {
    val assignableChannels: Flowable<Channel>
    val assignableRooms: Flowable<ChatRoom>

    val globalUserState: GlobalUserState

    fun send(message: String): Completable

    fun getFromChannel(channel: String): Maybe<ChannelUser>
    fun getFromChatRoom(id: String): Maybe<ChatRoomUser> = getFromChatRoom(UUID.fromString(id))
    fun getFromChatRoom(id: UUID): Maybe<ChatRoomUser>
}

interface ChatRoom {
    val id: UUID
    val roomData: io.glitchlib.model.ChatRoom
    val assignableChannel: Maybe<Channel>

    val users: Flowable<ChatRoomUser>

    val name: String
    val vips: Flowable<ChatRoomUser>
    val subscribers: Flowable<ChatRoomUser>
    val mods: Flowable<ChatRoomUser>

    val isEmoteOnly: Boolean
    val isR9k: Boolean
    val slow: Int
    val isSlow
        get() = slow > 0

    val slug: String
        get() = "${roomData.ownerId}:${roomData.id}"

    fun leave(): Completable
    fun send(message: String): Completable
    fun getUser(username: String): Maybe<ChatRoomUser> =
        users.filter { it.username == username }.firstElement()

    fun toggleColor(color: Color): Completable = toggleColor("#${color.rgb.toHexString().substring(2)}")
    fun toggleColor(color: String): Completable = send("/color $color")

    fun sendAction(message: String): Completable = send("/me $message")
    fun slow(duration: Duration = Duration.ofSeconds(30)): Completable = slow(duration.toSeconds())
    fun slow(duration: Long = 30): Completable = send("/slow${if (isSlow) "off" else " $duration"}")
    fun slowOff(): Completable = send("/slowoff")
    fun r9k(): Completable = send("/r9kbeta")
    fun r9kOff(): Completable = send("/r9kbetaoff")
    fun emoteOnly(): Completable = send("/emoteonly${if (isEmoteOnly) "off" else ""}")
}

interface ChatRoomUser : ChannelUserState {
    val username: String

    fun mod(): Completable
    fun ban(reason: String? = null): Completable
    fun timeout(seconds: Long, reason: String? = null): Completable
    fun timeout(duration: Duration, reason: String? = null) = timeout(duration.toSeconds(), reason)
    fun purge(reason: String? = null): Completable = timeout(1, reason)
    fun sendDM(message: String): Completable
}