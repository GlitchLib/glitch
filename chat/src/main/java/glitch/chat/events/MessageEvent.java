package glitch.chat.events;

import com.google.common.collect.ImmutableList;
import glitch.chat.irc.Emote;
import glitch.socket.utils.EventImmutable;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface MessageEvent extends UserStateEvent, ChannelUserEvent {
    long getChannelId();

    @Nullable
    String getMessage();

    boolean isActionMessage();

    ImmutableList<Emote> getEmotes();
}
