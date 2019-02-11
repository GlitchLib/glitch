package glitch.api.ws;

import glitch.api.ws.events.IEvent;
import glitch.service.ISocketService;

public interface IEventConverter<S extends ISocketService<S>> {
    IEvent<S> convert(S client, String raw);
}
