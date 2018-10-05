package glitch.chat.events;

import com.google.common.collect.ImmutableList;
import glitch.chat.irc.Emote;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface PrivateMessageEvent extends UserEvent, GlobalUserStateEvent {
    String getMessage();

    ImmutableList<Emote> getEmotes();
}
