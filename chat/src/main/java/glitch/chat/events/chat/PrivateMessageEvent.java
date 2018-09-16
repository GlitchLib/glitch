package glitch.chat.events.chat;

import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface PrivateMessageEvent extends UserEvent {
    String getMessage();
}
