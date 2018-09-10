package glitch.core.utils.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import glitch.core.utils.ws.event.CloseEventBuilder;
import glitch.core.utils.ws.event.PingEventBuilder;
import glitch.core.utils.ws.event.PongEventBuilder;
import glitch.core.utils.ws.event.SocketEventBuilder;
import glitch.core.utils.ws.event.ThrowableEventBuilder;
import glitch.core.utils.ws.event.message.RawByteMessageReceivedBuilder;
import glitch.core.utils.ws.event.message.RawByteMessageSendBuilder;
import glitch.core.utils.ws.event.message.RawMessageReceivedBuilder;
import glitch.core.utils.ws.event.message.RawMessageSendBuilder;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebSocketListener<S extends WebSocketClient> extends WebSocketAdapter {
    private final S ws;

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        ws.getClient().getEventManager().dispatch(new SocketEventBuilder<>().client(ws).build());
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        ws.getClient().getEventManager().dispatch(new ThrowableEventBuilder<>().throwable(cause).client(ws).build());
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        if (frame.isCloseFrame()) {
            ws.getClient().getEventManager().dispatch(new CloseEventBuilder<>().code(frame.getCloseCode()).reason(frame.getCloseReason()).client(ws).build());
        }
        if (frame.isPingFrame()) {
            ws.getClient().getEventManager().dispatch(new PingEventBuilder<>().client(ws).build());
        }
        if (frame.isPongFrame()) {
            ws.getClient().getEventManager().dispatch(new PongEventBuilder<>().client(ws).build());
        }
        if (frame.isTextFrame()) {
            ws.getClient().getEventManager().dispatch(new RawMessageSendBuilder<>().client(ws).build());
        }
        if (frame.isBinaryFrame()) {
            ws.getClient().getEventManager().dispatch(new RawByteMessageSendBuilder<>().client(ws).build());
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
        ws.getClient().getEventManager().dispatch(new RawByteMessageReceivedBuilder<>().client(ws).build());
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        ws.getClient().getEventManager().dispatch(new RawMessageReceivedBuilder<>().client(ws).build());
    }
}
