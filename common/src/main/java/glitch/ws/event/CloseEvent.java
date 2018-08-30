package glitch.ws.event;

import glitch.ws.WebSocketClient;
import java.time.Instant;

public class CloseEvent<S extends WebSocketClient> extends ConnectionEvent<S> {
    private final int code;
    private final String reason;

    public CloseEvent(int code, String reason, S client, Instant createdAt, String eventId) {
        super(client, createdAt, eventId);
        this.code = code;
        this.reason = reason;
    }
}
