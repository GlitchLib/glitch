package glitch.common.ws.event;

import glitch.common.events.Event;
import glitch.common.utils.Immutable;
import glitch.common.ws.WebSocketClient;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface SocketEvent<S extends WebSocketClient> extends Event {
    S getClient();
}
