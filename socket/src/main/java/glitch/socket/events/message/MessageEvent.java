package glitch.socket.events.message;

import glitch.core.utils.Immutable;
import glitch.socket.GlitchWebSocket;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import java.io.Serializable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface MessageEvent<R extends Serializable, S extends GlitchWebSocket> extends Event<S> {
    R getMessage();
}
