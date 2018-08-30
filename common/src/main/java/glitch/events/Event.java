package glitch.events;

import java.time.Instant;

public abstract class Event {
    private final Instant createdAt;
    private final String eventId;

    protected Event(Instant createdAt, String eventId) {
        this.createdAt = createdAt;
        this.eventId = eventId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getEventId() {
        return eventId;
    }
}
