package glitch.ws.event;

import glitch.events.Event;
import glitch.utils.Immutable;
import glitch.ws.WebSocketClient;
import java.time.Instant;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface SocketEvent<S extends WebSocketClient> extends Event {
    S getClient();
}
