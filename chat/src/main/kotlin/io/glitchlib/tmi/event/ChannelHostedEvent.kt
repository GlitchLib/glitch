package io.glitchlib.tmi.event

import io.glitchlib.GlitchClient
import io.glitchlib.model.IEvent

data class ChannelHostedEvent(
    override val client: GlitchClient,
    override val channelName: String,
    override val channelId: Long,
    val hostUsername: String,
    val views: Int,
    val autoHost: Boolean
) : IEvent, IChannel, IDChannel
