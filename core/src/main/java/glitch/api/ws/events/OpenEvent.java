package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;
import lombok.Data;
import lombok.ToString;

/**
 * Open Event emitting while connection in {@link AbstractWebSocketService} has started
 * @param <S> client extended {@link AbstractWebSocketService}
 */
@Data
public class OpenEvent<S extends AbstractWebSocketService<S>> implements IEvent<S> {
    @ToString.Exclude
    private final S client;
}
