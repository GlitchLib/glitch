package glitch.service;

import glitch.api.ws.WebSocket;
import glitch.api.ws.events.IEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Abstract WebSocket Client using {@link WebSocket} to handling connections into services like:
 * <b>Twitch Message Interface</b> {@code glitch-chat} and <b>Twitch PubSub</b> {@code glitch-pubsub}
 * <p>
 *
 * @param <S> extending {@link ISocketService this class}
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public interface ISocketService<S extends ISocketService<S>> extends IService {
    /**
     * Establish connection to the {@link glitch.api.ws.WebSocket} service
     *
     * @return to start connection use {@code {@link #login() login()}.{@link reactor.core.publisher.Mono#subscribe() subscribe()}}
     */
    Mono<Void> login();

    /**
     * Disconnecting from service
     */
    void logout();

    /**
     * Retrying connection from the service
     *
     * @return to start connection use {@code {@link #retry() retry()}.{@link reactor.core.publisher.Mono#subscribe() subscribe()}}
     */
    Mono<Void> retry();

    /**
     * Dispatch event using {@link reactor.core.publisher.FluxProcessor}
     *
     * @param type event type
     * @param <E>  event
     * @return events
     */
    <E extends IEvent<S>> Flux<E> onEvent(Class<E> type);
}
