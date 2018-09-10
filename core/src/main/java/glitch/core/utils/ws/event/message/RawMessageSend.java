package glitch.core.utils.ws.event.message;

import glitch.core.utils.Immutable;
import glitch.core.utils.ws.WebSocketClient;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface RawMessageSend<S extends WebSocketClient> extends MessageSend<String, S> {
}
