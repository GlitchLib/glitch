package glitch.api.ws.events

import glitch.service.ISocketService
import java.time.Instant
import java.util.*

/**
 * Event Interface
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface IEvent<S : ISocketService<S>> {
    /**
     * the [glitch.GlitchClient]
     */
    val client: S
    /**
     * Event creation time
     */
    val createdAt: Instant
        get() = Instant.now()

    /**
     * unique Event ID
     */
    val eventId: UUID
        get() = UUID.randomUUID()
}
