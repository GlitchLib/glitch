package glitch.chat.irc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import glitch.chat.IRCommand;

public interface IRCObject {
    String getRawMessage();
    ImmutableMap<String, Object> getTags();
    IRCPrefix getPrefix();
    IRCommand getCommand();
    ImmutableList<String> getTrailing();
    ImmutableList<String> getMiddle();
}
