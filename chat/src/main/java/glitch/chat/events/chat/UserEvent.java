package glitch.chat.events.chat;

import glitch.chat.GlitchChat;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface UserEvent extends Event<GlitchChat> {
    String getUsername();
}
