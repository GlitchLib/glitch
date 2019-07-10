package io.glitchlib.tmi.internal

import io.glitchlib.tmi.Channel
import io.glitchlib.tmi.ChannelUser
import io.glitchlib.tmi.event.ChannelUserState
import io.reactivex.Completable

class ChannelUserImpl(
    val channel: Channel,
    override val username: String,
    userState: ChannelUserState
) : ChannelUser, ChannelUserState by userState {

    override fun mod(): Completable =
        channel.send("/${if (isMod) "un" else ""}mod $username")

    override fun vip(): Completable =
        channel.send("/${if (isVip) "un" else ""}vip $username")

    override fun ban(reason: String?): Completable =
        channel.send("/ban $username${if (reason != null) " $reason" else ""}")

    override fun timeout(seconds: Long, reason: String?): Completable =
        channel.send("/timeout $username $seconds${if (reason != null) " $reason" else ""}")

    override fun sendDM(message: String): Completable =
        channel.send("/w $username $message")

}