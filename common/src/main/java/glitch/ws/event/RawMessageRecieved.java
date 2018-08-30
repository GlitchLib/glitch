package glitch.ws.event;

import glitch.ws.WebSocketClient;
import java.time.Instant;

public class RawMessageRecieved<S extends WebSocketClient> extends ConnectionEvent<S> {
    private final String message;
    public RawMessageRecieved(String message, S client, Instant createdAt, String eventId) {
        super(client, createdAt, eventId);
        this.message = message;
    }
}
