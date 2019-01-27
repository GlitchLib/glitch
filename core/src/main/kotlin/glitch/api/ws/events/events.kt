package glitch.api.ws.events

import glitch.api.ws.CloseStatus
import glitch.service.AbstractWebSocketService
import java.time.Instant
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
/**
 * All events must be extends this class if they want to be emitted via [reactor.core.publisher.FluxProcessor]
 * @param S client extended [glitch.service.AbstractWebSocketService]
 * @param client the [glitch.GlitchClient]
 * @param createdAt Event creation time
 * @param eventId unique Event ID
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
abstract class AbstractEvent<S : AbstractWebSocketService<S>> protected constructor(
        override val client: S,
        override val createdAt: Instant = Instant.now(),
        override val eventId: UUID = UUID.randomUUID()
) : IEvent<S>


/**
 * Emitting Close Event with [glitch.api.ws.CloseStatus]
 * @param S client extended [glitch.service.AbstractWebSocketService]
 * @param client the [glitch.GlitchClient]
 * @param code WebSocket close status code
 * @param reason WebSocket close status reason
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class CloseEvent<S : AbstractWebSocketService<S>>(
        override val client: S,
        val code: Int,
        val reason: String? = null
) : AbstractEvent<S>(client), IEvent<S> {
    constructor(client: S, status: CloseStatus): this(client, status.code, status.reason)
}

/**
 * Open Event emitting while connection in [glitch.service.AbstractWebSocketService] has started
 * @param S client extended [glitch.service.AbstractWebSocketService]
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class OpenEvent<S : AbstractWebSocketService<S>>(override val client: S) : AbstractEvent<S>(client), IEvent<S>


/**
 * Ping received
 * @param S client extended [glitch.service.AbstractWebSocketService]
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class PingEvent<S : AbstractWebSocketService<S>>(override val client: S) : AbstractEvent<S>(client)

/**
 * Pong received
 * @param S client extended [glitch.service.AbstractWebSocketService]
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class PongEvent<S : AbstractWebSocketService<S>>(override val client: S) : AbstractEvent<S>(client)