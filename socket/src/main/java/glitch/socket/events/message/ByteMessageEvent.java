package glitch.socket.events.message;

import glitch.socket.GlitchWebSocket;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface ByteMessageEvent<S extends GlitchWebSocket> extends Event<S> {
    byte[] getMessageBytes();
}
