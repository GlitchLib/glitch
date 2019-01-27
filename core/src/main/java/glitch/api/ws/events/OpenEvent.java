package glitch.api.ws.events;

import glitch.service.AbstractWebSocketService;
import java.util.StringJoiner;

/**
 * Open Event emitting while connection in {@link AbstractWebSocketService} has started
 * @param <S> client extended {@link AbstractWebSocketService}
 */
public class OpenEvent<S extends AbstractWebSocketService<S>> extends AbstractEvent<S> implements IEvent<S> {
    public OpenEvent(S client) {
        super(client);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("createdAt=" + getCreatedAt())
                .add("eventId=" + getEventId())
                .toString();
    }
}
