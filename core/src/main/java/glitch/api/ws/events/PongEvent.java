package glitch.api.ws.events;

import glitch.api.AbstractWebSocketService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PongEvent<S extends AbstractWebSocketService<S>> extends AbstractEvent<S> {
    public PongEvent(S client) {
        super(client);
    }
}
