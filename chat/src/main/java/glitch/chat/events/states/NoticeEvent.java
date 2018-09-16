package glitch.chat.events.states;

import glitch.chat.events.channel.ChannelEvent;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface NoticeEvent extends ChannelEvent {
    String getMessageId();

    String getMessage();
}
