package glitch.core.utils.ws.event;

import glitch.core.utils.Immutable;
import glitch.core.utils.ws.WebSocketClient;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface PingEvent<S extends WebSocketClient> extends SocketEvent<S> {
}
