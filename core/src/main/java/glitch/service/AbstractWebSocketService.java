package glitch.service;

import glitch.GlitchClient;
import glitch.api.ws.CloseStatus;
import glitch.api.ws.IConverter;
import glitch.api.ws.events.CloseEvent;
import glitch.api.ws.events.IEvent;
import glitch.api.ws.events.OpenEvent;
import java.net.URI;
import java.util.Objects;
import okhttp3.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;

/**
 * Abstract WebSocket Client using {@link WebSocket} to handling connections into services like:
 * <b>Twitch Message Interface</b> {@code glitch-chat} and <b>Twitch PubSub</b> {@code glitch-pubsub}
 * <p>
 *
 * @param <S> extending {@link AbstractWebSocketService this class}
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractWebSocketService<S extends AbstractWebSocketService<S>> implements IService {
    private final GlitchClient client;
    private final WebSocketClient ws;
    private final FluxProcessor<IEvent<S>, IEvent<S>> eventProcessor;
    private final IConverter<S> converter;

    /**
     * Constructor of {@link AbstractWebSocketService}
     * @param client the {@link GlitchClient}
     * @param uri WebSocket URI
     * @param converter Event {@link glitch.api.ws.IConverter Converter} wile responding
     */
    protected AbstractWebSocketService(
            GlitchClient client,
            String uri,
            FluxProcessor<IEvent<S>, IEvent<S>> eventProcessor,
            IConverter<S> converter) {
        Objects.requireNonNull(uri, "uri == null");
        if (!uri.matches("^ws(s)://(.+)")) {
            throw new IllegalArgumentException("URI must contain a WebSocket prefix.");
        }

        this.client = client;
        this.eventProcessor = eventProcessor;
        this.converter = converter;
        this.ws = new WebSocketClient(URI.create(uri), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                AbstractWebSocketService.this.eventProcessor.onNext(new OpenEvent<>((S) AbstractWebSocketService.this));
            }

            @Override
            public void onMessage(String message) {
                AbstractWebSocketService.this.eventProcessor.onNext(
                        AbstractWebSocketService.this.converter.convert((S) AbstractWebSocketService.this, message));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                AbstractWebSocketService.this.eventProcessor.onNext(new CloseEvent<>((S) AbstractWebSocketService.this, new CloseStatus(code, reason)));
            }

            @Override
            public void onError(Exception ex) {
                AbstractWebSocketService.this.eventProcessor.onError(ex);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlitchClient getClient() {
        return client;
    }

    public Mono<Void> disconnect() {
        return Mono.create(sink -> {
            this.ws.close();
            sink.success();
        });
    }

    public Mono<Void> retry() {
        return Mono.create(sink -> {
            this.ws.reconnect();
            sink.success();
        });
    }

    public Mono<Void> connect() {
        return Mono.create(sink -> {
            this.ws.connect();
            sink.success();
        });
    }

    public void sendRaw(Publisher<byte[]> raw) {
        Flux.from(raw).subscribe(ws::send);
    }

    public <E extends IEvent<S>> Flux<E> onEvent(Class<E> type) {
        return eventProcessor.ofType(type);
    }
}
