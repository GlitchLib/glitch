package glitch.api;

import glitch.GlitchClient;
import glitch.api.ws.CloseStatus;
import glitch.api.ws.Converter;
import glitch.api.ws.events.CloseEvent;
import glitch.api.ws.events.Event;
import glitch.api.ws.events.OpenEvent;
import glitch.api.ws.events.QueuedMessageEvent;
import glitch.exceptions.ws.RemoteCloseException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import okio.ByteString;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nullable;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

/**
 * Abstract WebSocket Client using {@link WebSocket} to handling connections into services like:
 * <b>Twitch Message Interface</b> {@code glitch-chat} and <b>Twitch PubSub</b> {@code glitch-pubsub}
 * <p>
 *
 * @param <S> extending {@link AbstractWebSocketService this class}
 * @param <E> extending {@link Event}
 */
@SuppressWarnings("unchecked")
public abstract class AbstractWebSocketService<S extends AbstractWebSocketService<S, E>, E extends Event<S>> {

    /**
     * Getting {@link GlitchClient}
     * @return the {@link GlitchClient Glitch Client}
     */
    @Getter
    private final GlitchClient client;
    private final Request request;
    private final FluxProcessor<E, E> eventProcessor;
    private final Converter<ByteString, E, S> eventConverter;

    private final AtomicBoolean closedByUser = new AtomicBoolean(false);

    @Setter(AccessLevel.PRIVATE)
    private WebSocket ws;

    /**
     * Constructor of {@link AbstractWebSocketService}
     * @param client the {@link GlitchClient}
     * @param uri WebSocket URI
     * @param eventProcessor Event Processor - using {@link Flux}
     * @param eventConverter Event {@link Converter} wile responding
     */
    protected AbstractWebSocketService(
            GlitchClient client,
            String uri,
            FluxProcessor<E, E> eventProcessor,
            Converter<ByteString, E, S> eventConverter) {
        Objects.requireNonNull(uri, "uri == null");
        if (!uri.matches("^ws(s)://(.+)")) {
            throw new IllegalArgumentException("URI must contain a WebSocket prefix.");
        }

        this.client = client;
        this.request = new Request.Builder().url(uri).build();
        this.eventProcessor = eventProcessor;
        this.eventConverter = eventConverter;
    }

    /**
     * Constructor of {@link AbstractWebSocketService}
     * @param client the {@link GlitchClient}
     * @param uri WebSocket URI
     * @param eventConverter Event {@link Converter} wile responding
     */
    protected AbstractWebSocketService(
            GlitchClient client,
            String uri,
            Converter<ByteString, E, S> eventConverter) {
        this(client, uri, EmitterProcessor.create(false), eventConverter);
    }

    public boolean isOpen() {
        return ws != null;
    }

    /**
     * Connecting to WebSocket Service
     * @return {@code {@link OpenEvent}} ready to subscribe
     * @throws AlreadyConnectedException WebSocket is already connected to their service
     */
    public Mono<OpenEvent<S>> connect() {
        return getEventProcessor()
                .doOnSubscribe(s -> doConnect())
                .ofType(OpenEvent.class)
                .map(e -> (OpenEvent<S>) e)
                .next();
    }

    public Flux<E> listenOn(Class<E> type) {
        return getEventProcessor()
                .ofType(type);
    }

    /**
     * Sending Message and adding them to {@link QueuedMessageEvent}
     * @param message byte message
     * @return {@code {@link QueuedMessageEvent}} ready to subscribe
     * @throws NotYetConnectedException WebSocket is not connected
     */
    protected Mono<QueuedMessageEvent<S>> send(byte[] message) {
        return getEventProcessor()
                .doOnSubscribe(s -> doQueueMessage(message))
                .ofType(QueuedMessageEvent.class)
                .map(e -> (QueuedMessageEvent<S>) e)
                .next();
    }

    /**
     * Sending Message and adding them to {@link QueuedMessageEvent}
     * @param message stringify message
     * @return {@code {@link QueuedMessageEvent}} ready to subscribe
     * @throws NotYetConnectedException WebSocket is not connected
     */
    protected Mono<QueuedMessageEvent<S>> send(String message) {
        return getEventProcessor()
                .doOnSubscribe(s -> doQueueMessage(message))
                .ofType(QueuedMessageEvent.class)
                .map(e -> (QueuedMessageEvent<S>) e)
                .next();
    }

    /**
     * Sending shutdown WebSocket
     * @param code {@link WebSocket} code
     * @param reason Reason
     * @return Shutting down WebSocket and handling {@code {@link CloseEvent}} ready to subscribe
     * @throws NotYetConnectedException WebSocket is not connected
     */
    public Mono<CloseEvent<S>> close(int code, String reason) {
        return getEventProcessor()
                .doOnSubscribe(s -> doDisconnect(code, reason))
                .ofType(CloseEvent.class)
                .map(e -> (CloseEvent<S>) e)
                .next();
    }

    /**
     * Getting Event Processor
     * @return {@link Event Events} composed into {@link Flux}
     */

    protected Flux<E> getEventProcessor() {
        return eventProcessor
                .subscribeOn(Schedulers.parallel())
                .log("glitch.http.ws", Level.ALL);
    }

    private void doConnect() {
        if (!isOpen()) {
            new OkHttpClient().newWebSocket(request, adaptListener());
        } else throw new AlreadyConnectedException();
    }

    private void doDisconnect(int code, String reason) {
        if (isOpen()) {
            closedByUser.set(true);
            ws.close(code, reason);
        } else throw new NotYetConnectedException();
    }

    private void doQueueMessage(byte[] message) {
        if (isOpen() && message != null) {
            ByteString bytes = ByteString.of(message);
            if (ws.send(bytes)) {
                eventProcessor.onNext((E) new QueuedMessageEvent<>((S) AbstractWebSocketService.this, bytes));
            }
        } else throw new NotYetConnectedException();
    }

    private void doQueueMessage(String message) {
        if (isOpen() && message != null) {
            if (ws.send(message)) {
                eventProcessor.onNext((E) new QueuedMessageEvent<>((S) AbstractWebSocketService.this, ByteString.encodeUtf8((String) message)));
            }
        }
    }

    private WebSocketListener adaptListener() {
        return new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                setWs(webSocket);
                eventProcessor.onNext((E) new OpenEvent<>((S) AbstractWebSocketService.this));
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                eventProcessor.onNext(eventConverter.convert(bytes, (S) AbstractWebSocketService.this));
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                CloseStatus status = new CloseStatus(code, reason);
                if (closedByUser.get()) {
                    eventProcessor.onNext((E) new CloseEvent<>((S) AbstractWebSocketService.this, status));
                    eventProcessor.onComplete();
                } else {
                    eventProcessor.onError(new RemoteCloseException(status));
                }
                setWs(null);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                eventProcessor.onError(t);
                setWs(null);
            }
        };
    }
}
