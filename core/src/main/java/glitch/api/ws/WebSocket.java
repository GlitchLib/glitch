package glitch.api.ws;

import glitch.api.ws.events.CloseEvent;
import glitch.api.ws.events.IEvent;
import glitch.api.ws.events.OpenEvent;
import glitch.exceptions.http.RequestException;
import glitch.exceptions.ws.AlreadyConnectedException;
import glitch.service.ISocketService;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import javax.annotation.Nullable;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class WebSocket<S extends ISocketService<S>> {
    private static final Logger LOG = LoggerFactory.getLogger("glitch.ws.connection");

    private final S client;

    private final OkHttpClient okHttp;
    private final Request request;
    private final FluxProcessor<IEvent<S>, IEvent<S>> eventProcessor;
    private final Scheduler eventScheduler;
    private final IEventConverter<S> eventConverter;
    private okhttp3.WebSocket realWS;

    public WebSocket(S client, OkHttpClient okHttp, Request request,
                     FluxProcessor<IEvent<S>, IEvent<S>> eventProcessor,
                     Scheduler eventScheduler, IEventConverter<S> eventConverter) {
        this.client = client;
        this.okHttp = okHttp;
        this.request = request;
        this.eventProcessor = eventProcessor;
        this.eventScheduler = eventScheduler;
        this.eventConverter = eventConverter;

    }

    public static <S extends ISocketService<S>> Builder<S> builder(S service) {
        return new Builder<>(service);
    }

    public boolean isConnected() {
        return this.realWS != null;
    }

    public Mono<Void> connect() {
        return Mono.<Void>create(sink -> {
            if (isConnected()) {
                sink.error(new AlreadyConnectedException());
            } else {
                this.realWS = okHttp.newWebSocket(request, listener());
                sink.success();
            }
        }).log("glitch.ws.connect", Level.FINE,
                SignalType.ON_NEXT, SignalType.ON_SUBSCRIBE, SignalType.ON_ERROR, SignalType.CANCEL);
    }

    @SuppressWarnings("unchecked")
    private WebSocketListener listener() {
        return new WebSocketListener() {
            @Override
            public void onOpen(okhttp3.WebSocket webSocket, Response response) {
                dispatch(new OpenEvent<>(client));
            }

            @Override
            public void onMessage(okhttp3.WebSocket webSocket, String text) {
                dispatch(eventConverter.convert(client, text));
            }

            @Override
            public void onClosed(okhttp3.WebSocket webSocket, int code, @Nullable String reason) {
                dispatch(new CloseEvent<>(client, code, reason));
            }

            @Override
            public void onFailure(okhttp3.WebSocket webSocket, Throwable t, @Nullable Response response) {
                eventProcessor.onError(t);
            }
        };
    }

    public void disconnect(int code, String reason) {
        if (isConnected()) {
            this.realWS.close(code, reason);
            this.realWS = null;
        }
    }

    public void disconnect() {
        disconnect(1000, null);
    }

    public Mono<Void> retry() {
        if (!isConnected()) {
            return Mono.error(new ExceptionInInitializerError("you must initialize first connection"));
        }

        return Mono.defer(() -> {
            disconnect(1000, "Retry");
            return connect();
        }).log("glitch.ws.reconnect", Level.FINE,
                SignalType.ON_NEXT, SignalType.ON_SUBSCRIBE, SignalType.ON_ERROR, SignalType.CANCEL);
    }

    public <E extends IEvent<S>> Flux<E> onEvent(Class<E> type) {
        return eventProcessor.publishOn(eventScheduler).ofType(type)
                .log("glitch.ws.event." + type.getSimpleName(), Level.FINE,
                        SignalType.ON_NEXT, SignalType.ON_SUBSCRIBE, SignalType.ON_ERROR, SignalType.CANCEL);
    }

    public void dispatch(IEvent<S> event) {
        eventProcessor.onNext(event);
    }

    public Mono<Void> send(Publisher<String> message) {
        return Flux.from(message)
                .log("glitch.ws.send", Level.FINE,
                        SignalType.ON_NEXT, SignalType.ON_SUBSCRIBE, SignalType.ON_ERROR, SignalType.CANCEL)
                .doOnNext(msg -> this.realWS.send(msg))
                .then();
    }

    public Flux<IEvent<S>> onEvents() {
        return eventProcessor.publishOn(eventScheduler)
                .log("glitch.ws.events.ALL", Level.FINE,
                        SignalType.ON_NEXT, SignalType.ON_SUBSCRIBE, SignalType.ON_ERROR, SignalType.CANCEL);
    }

    public static class Builder<S extends ISocketService<S>> {

        private final S service;

        private final OkHttpClient.Builder okHttp = new OkHttpClient.Builder();

        private FluxProcessor<IEvent<S>, IEvent<S>> eventProcessor = EmitterProcessor.create();
        private Scheduler eventScheduler = Schedulers.fromExecutor(Executors.newWorkStealingPool(), true);

        private IEventConverter<S> eventConverter;

        private Builder(S service) {
            this.service = service;
        }

        public Builder<S> addInterceptor(Interceptor interceptor) {
            this.okHttp.addInterceptor(interceptor);
            return this;
        }

        public Builder<S> setEventProcessor(FluxProcessor<IEvent<S>, IEvent<S>> eventProcessor) {
            this.eventProcessor = eventProcessor;
            return this;
        }

        public Builder<S> setEventScheduler(Scheduler eventScheduler) {
            this.eventScheduler = eventScheduler;
            return this;
        }

        public Builder<S> setEventConverter(IEventConverter<S> eventConverter) {
            this.eventConverter = eventConverter;
            return this;
        }

        public WebSocket<S> build(String url) {
            if (!Objects.requireNonNull(url, "url == null").matches("^ws(s)?://(.+)")) {
                throw new RequestException("URL it is not be a matched pattern!!! (ws / wss)");
            }

            if (okHttp.interceptors().stream().noneMatch(i -> i instanceof HttpLoggingInterceptor)) {
                okHttp.addInterceptor(new HttpLoggingInterceptor(LOG::debug).setLevel(HttpLoggingInterceptor.Level.BASIC));
            }

            Request request = new Request.Builder().url(url).get().build();

            return new WebSocket<>(service, okHttp.build(), request, eventProcessor, eventScheduler, Objects.requireNonNull(eventConverter, "Required defined Event Converter!"));
        }
    }
}
