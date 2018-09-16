package glitch.chat.events;

import glitch.chat.GlitchChat;
import glitch.chat.irc.IRCObject;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface RawIRCEvent extends IRCObject, Event<GlitchChat> {
}
