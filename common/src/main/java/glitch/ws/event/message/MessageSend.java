package glitch.ws.event.message;

import glitch.utils.Immutable;
import glitch.ws.WebSocketClient;
import glitch.ws.event.SocketEvent;
import java.io.Serializable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface MessageSend<R extends Serializable, S extends WebSocketClient> extends SocketEvent<S> {
    R getMessage();
}
