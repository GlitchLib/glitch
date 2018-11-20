package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;

import java.time.Instant;
import java.util.UUID;

public interface IEvent<S extends AbstractWebSocketService<S>> {
    S getClient();
    default Instant getCreatedAt() {
        return Instant.now();
    }
    default UUID getEventId() {
        return UUID.randomUUID();
    }
}
