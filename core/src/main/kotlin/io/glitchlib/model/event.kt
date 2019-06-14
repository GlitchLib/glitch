package io.glitchlib.model

import io.glitchlib.GlitchClient
import java.util.Date
import java.util.UUID

interface IEvent : GlitchObject, CreatedAt {
    val eventId: UUID
        get() = UUID.randomUUID()
    override val createdAt: Date
        get() = Date()
}

data class OpenEvent(
    override val client: GlitchClient
) : IEvent

data class CloseEvent(
    val status: Status,
    override val client: GlitchClient
) : IEvent {
    data class Status(
        val code: Int,
        val reason: String?
    )
}

data class PingEvent(
    override val client: GlitchClient
) : IEvent

data class PongEvent(
    override val client: GlitchClient
) : IEvent