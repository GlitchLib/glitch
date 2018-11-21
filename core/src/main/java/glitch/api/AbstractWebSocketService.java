package glitch.api;

import glitch.GlitchClient;
import glitch.api.ws.CloseStatus;
import glitch.api.ws.Converter;
import glitch.api.ws.events.*;
import glitch.exceptions.ws.ClosedByRemoteException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import okio.ByteString;
import org.reactivestreams.Publisher;
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
 */
@SuppressWarnings("unchecked")
public abstract class AbstractWebSocketService<S extends AbstractWebSocketService<S>> {

    /**
     * Getting {@link GlitchClient}
     * @return the {@link GlitchClient Glitch Client}
     */
    @Getter
    private final GlitchClient client;
    private final Request request;
    private final FluxProcessor<IEvent<S>, IEvent<S>> eventProcessor;
    private final Converter<S> eventConverter;

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
            FluxProcessor<IEvent<S>, IEvent<S>> eventProcessor,
            Converter<S> eventConverter) {
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
            Converter<S> eventConverter) {
        this(client, uri, EmitterProcessor.create(false), eventConverter);
    }

    public boolean isOpen() {
        return ws != null;
    }

    /**
     * Connecting to WebSocket Service
     * @return connection established
     * @throws AlreadyConnectedException WebSocket is already connected to their service
     */
    public Mono<Void> connect() {
        return Mono.fromCallable(() -> {
            if (!isOpen()) {
                return new OkHttpClient().newWebSocket(request, adaptListener());
            } else throw new AlreadyConnectedException();
        }).then();
    }

    public <E extends IEvent<S>> Flux<E> listenOn(Class<E> type) {
        return getEventProcessor()
                .ofType(type);
    }

    /**
     * Sending Message and adding them to {@link QueuedMessageEvent}
     * @param message stringify reactive message using {@link Publisher}
     * @return Message Send using non-blocking operation
     * @throws NotYetConnectedException WebSocket is not connected
     */
    public Mono<Void> send(Publisher<String> message) {
        return Flux.from(message).log()
                .doOnEach(stringSignal -> {
                    if (stringSignal.isOnNext()) {
                        doQueueMessage(stringSignal.get());
                    }
                }).then();
    }

    /**
     * Sending shutdown WebSocket
     *
     * @return Shutting down WebSocket
     * @throws NotYetConnectedException WebSocket is not connected
     */
    public Mono<Void> close() {
        return close(1000, "Disconnect");
    }

    /**
     * Sending shutdown WebSocket
     * @param code {@link WebSocket} code
     * @param reason Reason
     * @return Shutting down WebSocket
     * @throws NotYetConnectedException WebSocket is not connected
     */
    public Mono<Void> close(int code, String reason) {
        return Mono.fromCallable(() -> {
            if (isOpen()) {
                closedByUser.set(ws.close(code, reason));
                return null;
            } else throw new NotYetConnectedException();
        }).then();
    }

    /**
     * Getting Event Processor
     * @return {@link AbstractEvent Events} composed into {@link Flux}
     */

    protected <E extends IEvent<S>> Flux<E> getEventProcessor() {
        return (Flux<E>) eventProcessor
                .subscribeOn(Schedulers.parallel())
                .log("glitch.http.ws", Level.ALL);
    }

    private void doQueueMessage(String message) {
        if (isOpen() && message != null) {
            if (ws.send(message)) {
                eventProcessor.onNext(new QueuedMessageEvent<>((S) AbstractWebSocketService.this, ByteString.encodeUtf8(message)));
            }
        }
    }

    private WebSocketListener adaptListener() {
        return new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                setWs(webSocket);
                eventProcessor.onNext(new OpenEvent<>((S) AbstractWebSocketService.this));
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                eventProcessor.onNext(eventConverter.convert(bytes, (S) AbstractWebSocketService.this));
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                CloseStatus status = new CloseStatus(code, reason);
                if (closedByUser.get()) {
                    eventProcessor.onNext(new CloseEvent<>((S) AbstractWebSocketService.this, status));
                    eventProcessor.onComplete();
                } else {
                    eventProcessor.onError(new ClosedByRemoteException(status));
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
