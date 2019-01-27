package glitch.api.ws.events;

import glitch.service.AbstractWebSocketService;
import glitch.api.ws.CloseStatus;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Emitting Close Event with {@link CloseStatus}
 * @param <S> client extended {@link AbstractWebSocketService}
 */
public class CloseEvent<S extends AbstractWebSocketService<S>> extends AbstractEvent<S> implements IEvent<S> {
    private final int code;
    private final String reason;

    public CloseEvent(S client, CloseStatus status) {
        super(client);
        this.code = status.getCode();
        this.reason = status.getReason();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CloseEvent)) return false;
        if (!super.equals(o)) return false;
        CloseEvent<?> that = (CloseEvent<?>) o;
        return code == that.code &&
                Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code, reason);
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("code=" + code)
                .add("reason='" + reason + "'")
                .add("eventId='" + getEventId() + "'")
                .add("createdAt='" + getCreatedAt() + "'")
                .toString();
    }
}
