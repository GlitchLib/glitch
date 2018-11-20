package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

/**
 * All events must be extends this class if they want to be emitted via {@link reactor.core.publisher.FluxProcessor}
 * @param <S> client extended {@link AbstractWebSocketService}
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Event<S extends AbstractWebSocketService<S>> implements IEvent<S> {
    @ToString.Exclude
    private final S client;
    private final Instant createdAt;
    private final UUID eventId;

    protected Event(S client) {
        this(client, Instant.now());
    }

    protected Event(S client, Instant createdAt) {
        this(client, createdAt, UUID.randomUUID());
    }
}
