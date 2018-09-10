package glitch.core.utils.ws.event;

import glitch.core.events.Event;
import glitch.core.utils.Immutable;
import glitch.core.utils.ws.WebSocketClient;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface SocketEvent<S extends WebSocketClient> extends Event {
    S getClient();
}
