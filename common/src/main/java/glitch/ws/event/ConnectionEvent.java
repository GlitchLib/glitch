package glitch.ws.event;

import glitch.events.Event;
import glitch.ws.WebSocketClient;
import java.time.Instant;

public class ConnectionEvent<S extends WebSocketClient> extends Event {
    private final S client;

    public ConnectionEvent(S client, Instant createdAt, String eventId) {
        super(createdAt, eventId);
        this.client = client;
    }
}
