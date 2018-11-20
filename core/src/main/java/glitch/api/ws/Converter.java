package glitch.api.ws;

import glitch.api.AbstractWebSocketService;
import glitch.api.ws.events.IEvent;
import okio.ByteString;

public interface Converter<S extends AbstractWebSocketService<S>> {
    IEvent<S> convert(ByteString value, S client);
}
