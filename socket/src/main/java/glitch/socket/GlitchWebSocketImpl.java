package glitch.socket;

import glitch.GlitchClient;
import glitch.socket.events.Event;
import glitch.socket.events.actions.CloseEventImpl;
import glitch.socket.events.actions.OpenEventImpl;
import glitch.socket.events.actions.PingEventImpl;
import glitch.socket.events.actions.PongEventImpl;
import glitch.socket.events.actions.ThrowableEventImpl;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import lombok.Getter;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.PingFrame;
import org.java_websocket.framing.PongFrame;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GlitchWebSocketImpl extends WebSocketClient implements GlitchWebSocket {
    @Getter
    private final GlitchClient client;
    @Getter
    protected final PublishSubject<Event> dispatcher = PublishSubject.create();

    private final Logger logger = LoggerFactory.getLogger("glitch.socket.client");

    private final Function<String, PingFrame> pingFrame = message -> new PingFrame() {
        @Override
        public ByteBuffer getPayloadData() {
            return (message != null) ? Charset.forName("UTF-8").encode(message) : super.getPayloadData();
        }
    };
    private final Function<String, PongFrame> pongFrame = message -> new PongFrame() {
        @Override
        public ByteBuffer getPayloadData() {
            return (message != null) ? Charset.forName("UTF-8").encode(message) : super.getPayloadData();
        }
    };

    protected final AtomicReference<String> ping = new AtomicReference<>();
    protected final AtomicReference<String> pong = new AtomicReference<>();

    public GlitchWebSocketImpl(GlitchClient client, String uri) {
        super(URI.create(uri), new Draft_6455());
        this.client = client;
        dispatcher.timeInterval()
                .doOnEach(timed -> logger.debug("[EVENT] Call: {}", timed.getValue().value()))
                .doOnError(err -> logger.debug("[ERROR] [" + err.getClass().getSimpleName() +  "] " + err.getMessage(), err))
                .subscribe();
    }

    @Override
    public void sendPing() throws NotYetConnectedException {
        sendFrame(pingFrame.apply(ping.get()));
    }

    public void sendPong() throws NotYetConnectedException {
        sendFrame(pongFrame.apply(pong.get()));
    }

    @Override
    public void connect() {
        if (!isReady()) throw new RuntimeException("Is not ready yet!!!");
        super.connect();
    }

    private boolean isReady() {
        return ping.get() != null && !ping.get().equals("") &&
                pong.get() != null && !pong.get().equals("");
    }

    @Override
    public void disconnect() {
        this.close();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        onOpenEvent(handshakedata);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        onCloseEvent(code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        onExceptionEvent(ex);
    }

    @Override
    public void onWebsocketPing(WebSocket conn, Framedata f) {
        onPing(conn, f);
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {
        onPong(conn, f);
    }

    @SuppressWarnings("unchecked")
    public <S extends GlitchWebSocket, E extends Event<S>> Observable<E> listenOn(Class<E> eventType) {
        return dispatcher.ofType(eventType);
    }

    @SuppressWarnings("unchecked")
    private <S extends GlitchWebSocket> void onPing(WebSocket conn, Framedata f) {
        dispatcher.onNext(PingEventImpl.of(Instant.now(), UUID.randomUUID().toString(), (S) this));
        sendPong();
    }

    @SuppressWarnings("unchecked")
    private <S extends GlitchWebSocket> void onPong(WebSocket conn, Framedata f) {
        dispatcher.onNext(PongEventImpl.of(Instant.now(), UUID.randomUUID().toString(), (S) this));
    }

    @SuppressWarnings("unchecked")
    private <S extends GlitchWebSocket> void onOpenEvent(ServerHandshake handshakedata) {
        dispatcher.onNext(OpenEventImpl.of(Instant.now(), UUID.randomUUID().toString(), (S) this));
    }

    @SuppressWarnings("unchecked")
    private <S extends GlitchWebSocket> void onCloseEvent(int code, String reason, boolean remote) {
        dispatcher.onNext(CloseEventImpl.of(code, reason, remote, Instant.now(), UUID.randomUUID().toString(), (S) this));
    }

    @SuppressWarnings("unchecked")
    private <S extends GlitchWebSocket> void onExceptionEvent(Exception ex) {
        dispatcher.onNext(ThrowableEventImpl.of(ex, Instant.now(), UUID.randomUUID().toString(), (S) this));
    }
}
