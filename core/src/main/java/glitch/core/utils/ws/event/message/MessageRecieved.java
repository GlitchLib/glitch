package glitch.core.utils.ws.event.message;

import glitch.core.utils.Immutable;
import glitch.core.utils.ws.WebSocketClient;
import glitch.core.utils.ws.event.SocketEvent;
import java.io.Serializable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface MessageRecieved<R extends Serializable, S extends WebSocketClient> extends SocketEvent<S> {
    R getMessage();
}
