package glitch.socket.events.message;

import glitch.socket.GlitchWebSocket;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import java.nio.ByteBuffer;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface RawByteMessageEvent<S extends GlitchWebSocket> extends Event<S> {
    ByteBuffer getMessageBytes();
}
