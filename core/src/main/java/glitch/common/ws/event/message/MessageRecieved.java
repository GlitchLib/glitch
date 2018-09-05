package glitch.common.ws.event.message;

import glitch.common.utils.Immutable;
import glitch.common.ws.WebSocketClient;
import glitch.common.ws.event.SocketEvent;
import java.io.Serializable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface MessageRecieved<R extends Serializable, S extends WebSocketClient> extends SocketEvent<S> {
    R getMessage();
}
