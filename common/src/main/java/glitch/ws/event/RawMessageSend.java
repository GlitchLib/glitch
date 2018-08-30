package glitch.ws.event;

import glitch.ws.WebSocketClient;
import java.time.Instant;

public class RawMessageSend<S extends WebSocketClient> extends ConnectionEvent<S> {
    private final String message;
    public RawMessageSend(String message, S client, Instant createdAt, String eventId) {
        super(client, createdAt, eventId);
        this.message = message;
    }
}
