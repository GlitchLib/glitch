package glitch.common.ws.event;

import glitch.common.utils.Immutable;
import glitch.common.ws.WebSocketClient;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface CloseEvent<S extends WebSocketClient> extends SocketEvent<S> {
    int getCode();
    String getReason();
}
