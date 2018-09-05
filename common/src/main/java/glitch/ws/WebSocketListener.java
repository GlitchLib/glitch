package glitch.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import glitch.ws.event.CloseEventBuilder;
import glitch.ws.event.PingEventBuilder;
import glitch.ws.event.PongEventBuilder;
import glitch.ws.event.SocketEventBuilder;
import glitch.ws.event.ThrowableEventBuilder;
import glitch.ws.event.message.RawByteMessageReceivedBuilder;
import glitch.ws.event.message.RawByteMessageSendBuilder;
import glitch.ws.event.message.RawMessageReceivedBuilder;
import glitch.ws.event.message.RawMessageSendBuilder;
import java.util.List;
import java.util.Map;

public class WebSocketListener<S extends WebSocketClient> extends WebSocketAdapter {
    private final S client;
    WebSocketListener(S client) {
        this.client = client;
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        client.manager.dispatch(new SocketEventBuilder<>().client(client).build());
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        client.manager.dispatch(new ThrowableEventBuilder<>().throwable(cause).client(client).build());
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        if (frame.isCloseFrame()) {
            client.manager.dispatch(new CloseEventBuilder<>().code(frame.getCloseCode()).reason(frame.getCloseReason()).client(client).build());
        }
        if (frame.isPingFrame()) {
            client.manager.dispatch(new PingEventBuilder<>().client(client).build());
        }
        if (frame.isPongFrame()) {
            client.manager.dispatch(new PongEventBuilder<>().client(client).build());
        }
        if (frame.isTextFrame()) {
            client.manager.dispatch(new RawMessageSendBuilder<>().client(client).build());
        }
        if (frame.isBinaryFrame()) {
            client.manager.dispatch(new RawByteMessageSendBuilder<>().client(client).build());
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
        client.manager.dispatch(new RawByteMessageReceivedBuilder<>().client(client).build());
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        client.manager.dispatch(new RawMessageReceivedBuilder<>().client(client).build());
    }
}
