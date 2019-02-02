package glitch.api.ws;

import glitch.api.ws.events.IEvent;
import glitch.service.AbstractWebSocketService;

public interface IConverter<S extends AbstractWebSocketService<S>> {
    IEvent<S> convert(S client, String raw);
}
