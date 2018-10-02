package glitch.chat.events;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import glitch.chat.GlitchChat;
import glitch.chat.IRCommand;
import glitch.chat.irc.IRCPrefix;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface RawIRCEvent extends Event<GlitchChat> {

    String getRawMessage();

    ImmutableMap<String, String> getTags();

    IRCPrefix getPrefix();

    IRCommand getCommand();

    ImmutableList<String> getTrailing();

    ImmutableList<String> getMiddle();
}
