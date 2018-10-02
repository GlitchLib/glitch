package glitch.chat.events;

import com.google.common.collect.ImmutableMap;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface MessageEvent extends ChannelUserEvent {
    ImmutableMap<String, String> getTags();

    String getMessage();

    boolean isActionMessage();
}
