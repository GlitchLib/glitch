package glitch.api.ws;

import glitch.api.AbstractWebSocketService;
import glitch.api.ws.events.Event;

public interface Converter<F, E extends Event<S>, S extends AbstractWebSocketService<S, E>> {
    E convert(F value, S client);
}
