package glitch.api.ws.events;

import glitch.service.AbstractWebSocketService;
import java.util.StringJoiner;

public class PingEvent<S extends AbstractWebSocketService<S>> extends AbstractEvent<S> {
    public PingEvent(S client) {
        super(client);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("createdAt=" + getCreatedAt())
                .add("eventId=" + getEventId())
                .toString();
    }
}
