package glitch.ws.event;

import glitch.ws.WebSocketClient;
import java.time.Instant;

public class PongEvent<S extends WebSocketClient> extends ConnectionEvent<S> {
    public PongEvent(S client, Instant createdAt, String eventId) {
        super(client, createdAt, eventId);
    }
}
