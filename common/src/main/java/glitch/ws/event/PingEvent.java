package glitch.ws.event;

import glitch.ws.WebSocketClient;
import java.time.Instant;

public class PingEvent<S extends WebSocketClient> extends ConnectionEvent<S> {
    public PingEvent(S client, Instant createdAt, String eventId) {
        super(client, createdAt, eventId);
    }
}
