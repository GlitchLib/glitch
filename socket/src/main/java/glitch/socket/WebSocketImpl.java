package glitch.socket;

import glitch.GlitchClient;
import glitch.socket.events.Event;
import glitch.socket.events.actions.CloseEvent;
import glitch.socket.events.actions.CloseEventImpl;
import glitch.socket.events.actions.OpenEventImpl;
import glitch.socket.events.message.RawByteMessageEventImpl;
import glitch.socket.events.message.RawMessageEventImpl;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Instant;
import lombok.Getter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketImpl implements GlitchWebSocket {

    @Getter
    protected final PublishSubject<Event> dispatcher = PublishSubject.create();
    @Getter
    private final GlitchClient client;
    private final WebSocketClient ws;

    public WebSocketImpl(GlitchClient client, String uri) {
        this.client = client;
        this.ws = new WebSocketClient(URI.create(uri), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                dispatcher.onNext(OpenEventImpl.of(Instant.now(), WebSocketImpl.this));
            }

            @Override
            public void onMessage(String message) {
                dispatcher.onNext(RawMessageEventImpl.of(message, Instant.now(), WebSocketImpl.this));
            }

            @Override
            public void onMessage(ByteBuffer buffer) {
                dispatcher.onNext(RawByteMessageEventImpl.of(buffer, Instant.now(), WebSocketImpl.this));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                dispatcher.onNext(CloseEventImpl.of(code, reason, remote, Instant.now(), WebSocketImpl.this));
            }

            @Override
            public void onError(Exception ex) {
                dispatcher.onError(ex);
            }
        };
    }

    @Override
    public Single<Void> connect() {
        return Single.create(ignore -> ws.connectBlocking());
    }

    @Override
    public Single<Void> disconnect() {
        return Single.create(ignore -> ws.closeBlocking());
    }

    @Override
    public Single<Void> reconnect() {
        return Single.create(ignore -> ws.reconnectBlocking());
    }

    @Override
    public <S extends GlitchWebSocket, E extends Event<S>> Observable<E> listenOn(Class<E> eventType) {
        return dispatcher.ofType(eventType);
    }
}
