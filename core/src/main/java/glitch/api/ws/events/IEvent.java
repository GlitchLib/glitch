package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;

import java.time.Instant;
import java.util.UUID;

public interface IEvent<S extends AbstractWebSocketService<S>> {
    S getClient();
    Instant getCreatedAt();
    UUID getEventId();
}
