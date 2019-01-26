package glitch.api.ws;

import glitch.service.AbstractWebSocketService;
import glitch.api.ws.events.IEvent;

public interface IConverter<S extends AbstractWebSocketService<S>> {
    IEvent<S> convert(S client, String raw);
}
