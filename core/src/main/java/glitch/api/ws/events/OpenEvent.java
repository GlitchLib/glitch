package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Open Event emitting while connection in {@link AbstractWebSocketService} has started
 * @param <S> client extended {@link AbstractWebSocketService}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenEvent<S extends AbstractWebSocketService<S, ? extends Event<S>>> extends Event<S> {
    public OpenEvent(S client) {
        super(client);
    }
}
