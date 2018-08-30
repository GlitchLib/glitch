package glitch.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import glitch.ws.event.CloseEvent;
import glitch.ws.event.ConnectionEvent;
import glitch.ws.event.PingEvent;
import glitch.ws.event.PongEvent;
import glitch.ws.event.RawByteMessageRecieved;
import glitch.ws.event.RawByteMessageSend;
import glitch.ws.event.RawMessageRecieved;
import glitch.ws.event.RawMessageSend;
import glitch.ws.event.ThrowableEvent;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WebSocketListener<S extends WebSocketClient> extends WebSocketAdapter {
    private final S client;
    WebSocketListener(S client) {
        this.client = client;
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        client.eventBus.dispatch(new ConnectionEvent<>(client, Instant.now(), UUID.randomUUID().toString()));
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        client.eventBus.dispatch(new ThrowableEvent<>(cause, client, Instant.now(), UUID.randomUUID().toString()));
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        if (frame.isCloseFrame()) {
            client.eventBus.dispatch(new CloseEvent<>(frame.getCloseCode(), frame.getCloseReason(), client, Instant.now(), UUID.randomUUID().toString()));
        }
        if (frame.isPingFrame()) {
            client.eventBus.dispatch(new PingEvent<>(client, Instant.now(), UUID.randomUUID().toString()));
        }
        if (frame.isPongFrame()) {
            client.eventBus.dispatch(new PongEvent<>(client, Instant.now(), UUID.randomUUID().toString()));
        }
        if (frame.isTextFrame()) {
            client.eventBus.dispatch(new RawMessageSend<>(frame.getPayloadText(), client, Instant.now(), UUID.randomUUID().toString()));
        }
        if (frame.isBinaryFrame()) {
            client.eventBus.dispatch(new RawByteMessageSend<>(frame.getPayload(), client, Instant.now(), UUID.randomUUID().toString()));
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
        client.eventBus.dispatch(new RawByteMessageRecieved<>(binary, client, Instant.now(), UUID.randomUUID().toString()));
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        client.eventBus.dispatch(new RawMessageRecieved<>(text, client, Instant.now(), UUID.randomUUID().toString()));
    }
}
