package glitch.chat.events;

import glitch.chat.GlitchChat;
import glitch.chat.IRCCommand;
import glitch.chat.irc.IRCPrefix;
import glitch.chat.irc.Tags;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import java.util.List;
import javax.annotation.Nullable;
import org.immutables.value.Value;

/**
 * The raw IRC event formatted into JSON Object
 * <p>
 * Event has been presented by following patterns:
 * {@code @&lt;tags&gt; &lt;prefix&gt; &lt;command&gt; [&lt;middle&gt; | &lt;middle&gt; :&lt;trailing&gt;]}
 */
@EventImmutable
@Value.Immutable(builder = true)
public interface RawIRCEvent extends Event<GlitchChat> {

    /**
     * Unformatted IRC message
     *
     * @return Raw Message, formatted into IRC
     */
    String getRawMessage();

    /**
     * Tags parameters. Will be empty if some {@link glitch.chat.IRCCommand {@code Commands}} doesn't even support it.
     *
     * @return Tag object
     */
    Tags getTags();

    /**
     * Prefix of IRC Message
     *
     * @return IRC Prefix
     */
    IRCPrefix getPrefix();

    /**
     * Specific IRC Command. If some commands isn't match from {@link glitch.chat.IRCCommand commands} will be return {@link glitch.chat.IRCCommand#UNKNOWN {@code UNKNOWN}}
     *
     * @return IRC Command
     */
    IRCCommand getCommand();

    /**
     * Middle parameters. Usually contains channel name.
     *
     * @return Middle parameters in {@link java.util.List Immutable List}
     */
    List<String> getMiddle();

    /**
     * The leftover parameters after {@link #getMiddle() Middle Parameters} splitted with double dot colon ({@code :}). Generally it is a message from the channel, user or server.
     *
     * @return the leftover middle parameters splitted with double dot colon ({@code :})
     */
    @Nullable
    String getTrailing();
}
