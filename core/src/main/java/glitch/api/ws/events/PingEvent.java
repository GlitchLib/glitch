package glitch.api.ws.events;

import glitch.service.AbstractWebSocketService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PingEvent<S extends AbstractWebSocketService<S>> extends AbstractEvent<S> {
    public PingEvent(S client) {
        super(client);
    }
}
