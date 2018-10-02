package glitch.chat.events;

import glitch.chat.GlitchChat;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface ChannelEvent extends Event<GlitchChat> {
    String getChannel();
}
