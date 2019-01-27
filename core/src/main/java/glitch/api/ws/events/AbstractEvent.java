package glitch.api.ws.events;

import glitch.service.AbstractWebSocketService;
import java.util.Objects;

import java.time.Instant;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * All events must be extends this class if they want to be emitted via {@link reactor.core.publisher.FluxProcessor}
 * @param <S> client extended {@link AbstractWebSocketService}
 */
public abstract class AbstractEvent<S extends AbstractWebSocketService<S>> implements IEvent<S> {
    private final S client;
    private final Instant createdAt;
    private final UUID eventId;

    protected AbstractEvent(S client, Instant createdAt, UUID eventId) {
        this.client = client;
        this.createdAt = createdAt;
        this.eventId = eventId;
    }

    protected AbstractEvent(S client) {
        this(client, Instant.now());
    }

    protected AbstractEvent(S client, Instant createdAt) {
        this(client, createdAt, UUID.randomUUID());
    }

    @Override
    public S getClient() {
        return client;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEvent)) return false;
        AbstractEvent<?> that = (AbstractEvent<?>) o;
        return getCreatedAt().equals(that.getCreatedAt()) &&
                getEventId().equals(that.getEventId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCreatedAt(), getEventId());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("createdAt=" + createdAt)
                .add("eventId=" + eventId)
                .toString();
    }
}
