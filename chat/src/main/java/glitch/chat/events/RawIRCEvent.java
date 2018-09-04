package glitch.chat.events;

import glitch.chat.MessageInterface;
import glitch.chat.irc.IRCObject;
import glitch.common.utils.Immutable;
import glitch.common.ws.event.SocketEvent;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface RawIRCEvent extends IRCObject, SocketEvent<MessageInterface> {
}
