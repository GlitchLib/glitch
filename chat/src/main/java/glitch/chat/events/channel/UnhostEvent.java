package glitch.chat.events.channel;

import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface UnhostEvent extends ChannelEvent {
}
