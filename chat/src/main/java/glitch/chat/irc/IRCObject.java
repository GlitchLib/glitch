package glitch.chat.irc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import glitch.chat.utils.IRCommand;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface IRCObject {
    String getRawMessage();

    ImmutableMap<String, String> getTags();

    IRCPrefix getPrefix();

    IRCommand getCommand();

    ImmutableList<String> getTrailing();

    ImmutableList<String> getMiddle();
}
