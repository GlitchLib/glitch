package glitch.socket.events.message;

import glitch.socket.GlitchWebSocket;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface RawMessageEvent<S extends GlitchWebSocket> extends MessageEvent<String, S> {
}
