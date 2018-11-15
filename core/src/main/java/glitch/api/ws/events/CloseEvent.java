package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;
import glitch.api.ws.CloseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Emitting Close Event with {@link CloseStatus}
 * @param <S> client extended {@link AbstractWebSocketService}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloseEvent<S extends AbstractWebSocketService<S, ? extends Event<S>>> extends Event<S> {
    private final CloseStatus status;

    public CloseEvent(S client, CloseStatus status) {
        super(client);
        this.status = status;
    }
}
