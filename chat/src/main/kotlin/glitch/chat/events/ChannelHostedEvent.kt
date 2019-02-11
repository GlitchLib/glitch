package glitch.chat.events

import glitch.api.ws.events.IEvent
import glitch.chat.GlitchChat

data class ChannelHostedEvent(
        override val client: GlitchChat,
        override val channelName: String,
        override val channelId: Long,
        val hostUsername: String,
        val views: Int,
        val autoHost: Boolean
) : IEvent<GlitchChat>, IChannel, IDChannel
