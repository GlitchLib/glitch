package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;
import glitch.api.ws.CloseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Emitting Close Event with {@link CloseStatus}
 * @param <S> client extended {@link AbstractWebSocketService}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloseEvent<S extends AbstractWebSocketService<S>> extends CloseStatus implements IEvent<S> {
    @ToString.Exclude
    private final S client;

    public CloseEvent(S client, CloseStatus status) {
        super(status.getCode(), status.getReason());
        this.client = client;
    }
}
