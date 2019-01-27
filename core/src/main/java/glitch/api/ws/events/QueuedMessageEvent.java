package glitch.api.ws.events;

import glitch.service.AbstractWebSocketService;
import java.util.StringJoiner;
import okio.ByteString;

/**
 * Queueing Message emits while you sending data into server
 * @param <S> client extended {@link AbstractWebSocketService}
 */
public class QueuedMessageEvent<S extends AbstractWebSocketService<S>> extends AbstractEvent<S> implements IEvent<S> {
    private final ByteString message;

    public QueuedMessageEvent(S client, ByteString message) {
        super(client);
        this.message = message;
    }
    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("createdAt=" + getCreatedAt())
                .add("eventId=" + getEventId())
                .add("message='" + message + "'")
                .toString();
    }
}
