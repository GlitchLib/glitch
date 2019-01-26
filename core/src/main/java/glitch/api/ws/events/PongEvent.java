package glitch.api.ws.events;

import glitch.service.AbstractWebSocketService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PongEvent<S extends AbstractWebSocketService<S>> extends AbstractEvent<S> {
    public PongEvent(S client) {
        super(client);
    }
}
