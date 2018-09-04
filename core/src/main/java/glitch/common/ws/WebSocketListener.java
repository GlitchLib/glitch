package glitch.common.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import glitch.common.ws.event.CloseEventBuilder;
import glitch.common.ws.event.PingEventBuilder;
import glitch.common.ws.event.PongEventBuilder;
import glitch.common.ws.event.SocketEventBuilder;
import glitch.common.ws.event.ThrowableEventBuilder;
import glitch.common.ws.event.message.RawByteMessageReceivedBuilder;
import glitch.common.ws.event.message.RawByteMessageSendBuilder;
import glitch.common.ws.event.message.RawMessageReceivedBuilder;
import glitch.common.ws.event.message.RawMessageSendBuilder;
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
