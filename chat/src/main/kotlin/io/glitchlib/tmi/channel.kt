package io.glitchlib.tmi

import io.glitchlib.model.ChatRoom
import io.glitchlib.tmi.event.ChannelUserState
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import okhttp3.internal.toHexString
import java.awt.Color
import java.time.Duration
import kotlin.math.abs

private val COMMERCIAL_DURATION = setOf(30, 60, 90, 120, 150, 180)

interface Channel {
    val name: String
    val rooms: Flowable<ChatRoom>
    val users: Flowable<ChannelUser>
    val vips: Flowable<ChannelUser>
        get() = users.filter { it.isVip }
    val subscribers: Flowable<ChannelUser>
        get() = users.filter { it.isSubscriber }
    val mods: Flowable<ChannelUser>
        get() = users.filter { it.isMod }

    val isEmoteOnly: Boolean
    val follows: Int
    val isR9k: Boolean
    val slow: Int
    val isSubOnly: Boolean
    val isFollows
        get() = follows > -1
    val isSlow
        get() = slow > 0

    fun leave(): Completable
    fun send(message: String): Completable
    fun getUser(username: String): Maybe<ChannelUser> =
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
    fun subscribers(): Completable = send("/subscribers${if (isSubOnly) "off" else ""}")
    fun followers(duration: Duration = Duration.ZERO): Completable = followers(duration.toMinutes())
    fun followers(duration: Long = 0L): Completable = send("/followers ${if (duration < 0) "10" else "$duration"}m")
    fun followersOff(): Completable = send("/followersoff")
    fun commercial(duration: Duration = Duration.ofSeconds(30)): Completable = commercial(duration.toSeconds())
    fun commercial(duration: Long = 30L): Completable =
        send("/commercial ${COMMERCIAL_DURATION.minBy { abs(it - duration) } ?: 30L}")

    fun host(channel: String): Completable = send("/host $channel")
    fun unhost(channel: String): Completable = send("/unhost")
    fun raid(channel: String): Completable = send("/raid $channel")
    fun unraid(channel: String): Completable = send("/unraid")
    fun marker(description: String? = null): Completable =
        send("/marker${if (description != null) " $description" else ""}")
}

interface ChannelUser : ChannelUserState {
    val username: String

    fun mod(): Completable
    fun vip(): Completable
    fun ban(reason: String? = null): Completable
    fun timeout(seconds: Long, reason: String? = null): Completable
    fun timeout(duration: Duration, reason: String? = null) = timeout(duration.toSeconds(), reason)
    fun purge(reason: String? = null): Completable = timeout(1, reason)
    fun sendDM(message: String): Completable
}