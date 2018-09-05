package glitch.ws.event.message;

import glitch.utils.Immutable;
import glitch.ws.WebSocketClient;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface RawMessageReceived<S extends WebSocketClient> extends MessageRecieved<String, S> {}
