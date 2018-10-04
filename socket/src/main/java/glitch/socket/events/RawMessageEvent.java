package glitch.socket.events;

import glitch.socket.GlitchWebSocket;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface RawMessageEvent<S extends GlitchWebSocket> extends Event<S> {
    String getMessage();
}
