package glitch.api.ws.events;

import glitch.service.AbstractWebSocketService;
import glitch.api.ws.CloseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Emitting Close Event with {@link CloseStatus}
 * @param <S> client extended {@link AbstractWebSocketService}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloseEvent<S extends AbstractWebSocketService<S>> extends AbstractEvent<S> implements IEvent<S> {
    private final int code;
    private final String reason;

    public CloseEvent(S client, CloseStatus status) {
        super(client);
        this.code = status.getCode();
        this.reason = status.getReason();
    }
}
