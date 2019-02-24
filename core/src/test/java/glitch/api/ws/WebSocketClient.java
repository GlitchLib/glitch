package glitch.api.ws;

import com.google.gson.Gson;
import glitch.GlitchClient;
import glitch.GlitchClientTest;
import glitch.api.ws.events.IEvent;
import glitch.service.ISocketService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class WebSocketClient implements ISocketService<WebSocketClient> {
    private final WebSocket<WebSocketClient> ws = WebSocket.builder(this)
            .setEventConverter((client, raw) -> new MessageEvent(client, new Gson().toJsonTree(raw)))
            .build("ws://demos.kaazing.com/echo");

    @Override
    public Mono<Void> login() {
        return ws.connect();
    }

    @Override
    public void logout() {
        ws.disconnect();
    }

    @Override
    public Mono<Void> retry() {
        return ws.retry();
    }

    @Override
    public <E extends IEvent<WebSocketClient>> Flux<E> onEvent(Class<E> type) {
        return ws.onEvent(type);
    }

    @Override
    public Flux<IEvent<WebSocketClient>> onEvents() {
        return this.ws.onEvents();
    }

    @Override
    public GlitchClient getClient() {
        return GlitchClientTest.CLIENT;
    }

    Mono<Void> send(String message) {
        return ws.send(Mono.just(message));
    }
}
