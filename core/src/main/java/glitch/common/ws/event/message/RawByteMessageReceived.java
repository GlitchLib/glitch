package glitch.common.ws.event.message;

import glitch.common.utils.Immutable;
import glitch.common.ws.WebSocketClient;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface RawByteMessageReceived<S extends WebSocketClient> extends MessageRecieved<Byte[], S> {}
