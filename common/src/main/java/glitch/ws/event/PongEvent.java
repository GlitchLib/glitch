package glitch.ws.event;

import glitch.utils.Immutable;
import glitch.ws.WebSocketClient;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface PongEvent<S extends WebSocketClient> extends SocketEvent<S> {}
