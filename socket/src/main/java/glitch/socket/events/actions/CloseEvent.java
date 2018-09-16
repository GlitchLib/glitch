package glitch.socket.events.actions;

import glitch.socket.GlitchWebSocket;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface CloseEvent<S extends GlitchWebSocket> extends Event<S> {
    int getCode();

    String getReason();

    boolean isRemote();
}
