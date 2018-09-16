package glitch.chat.events.channel;

import glitch.chat.events.chat.UserEvent;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface ChannelUserEvent extends ChannelEvent, UserEvent {
}
