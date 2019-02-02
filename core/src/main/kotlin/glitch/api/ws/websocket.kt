package glitch.api.ws

import glitch.api.ws.events.IEvent
import glitch.service.AbstractWebSocketService
import reactor.core.publisher.Flux

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
inline fun <reified E : IEvent<S>, S : AbstractWebSocketService<S>> S.onEvent(): Flux<E> = onEvent(E::class.java)