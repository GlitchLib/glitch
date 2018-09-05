package glitch.chat.events;

import glitch.chat.MessageInterface;
import glitch.chat.irc.IRCObject;
import glitch.utils.Immutable;
import glitch.ws.event.SocketEvent;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface RawIRCEvent extends IRCObject, SocketEvent<MessageInterface> {
}
