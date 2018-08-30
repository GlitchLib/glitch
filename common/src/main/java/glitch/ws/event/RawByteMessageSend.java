package glitch.ws.event;

import glitch.ws.WebSocketClient;
import java.time.Instant;

public class RawByteMessageSend<S extends WebSocketClient> extends ConnectionEvent<S> {
    private final byte[] message;
    public RawByteMessageSend(byte[] message, S client, Instant createdAt, String eventId) {
        super(client, createdAt, eventId);
        this.message = message;
    }
}
