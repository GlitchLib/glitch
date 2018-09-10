package glitch.chat.events;

import glitch.chat.MessageInterface;
import glitch.chat.irc.IRCObject;
import glitch.core.utils.Immutable;
import glitch.core.utils.ws.event.SocketEvent;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface RawIRCEvent extends IRCObject, SocketEvent<MessageInterface> {
}
