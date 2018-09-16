package glitch.chat.events.states;

import glitch.chat.GlitchChat;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import java.awt.Color;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface GlobalUserStateEvent extends Event<GlitchChat> {
    Color getColor();

    String getDisplayName();

    boolean hasTurbo();

    long getUserId();

    String getUserType();
}
