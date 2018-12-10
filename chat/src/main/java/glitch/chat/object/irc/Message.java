package glitch.chat.object.irc;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import javax.annotation.Nullable;
import java.util.List;

@Data
@Builder(builderClassName = "Builder")
public class Message {

    /**
     * Unformatted IRC message
     *
     * @return Raw Message, formatted into IRC
     */
    private final String rawMessage;

    /**
     * Tags parameters. Will be empty if some {@link Tags {@code Commands}} doesn't even support it.
     *
     * @return Tag object
     */
    private final Tags tags;

    /**
     * Prefix of IRC Message
     *
     * @return IRC Prefix
     */
    private final Prefix prefix;

    /**
     * Specific IRC Command. If some commands isn't match from {@link Command commands} will be return {@link Command#UNKNOWN {@code UNKNOWN}}
     *
     * @return IRC Command
     */
    private final Command command;

    /**
     * Middle parameters. Usually contains channel name.
     *
     * @return Middle parameters in {@link java.util.List Immutable List}
     */
    @Singular("middle")
    private final List<String> middle;

    /**
     * The leftover parameters after {@link #getMiddle() Middle Parameters} splitted with double dot colon ({@code :}). Generally it is a message from the channel, user or server.
     *
     * @return the leftover middle parameters splitted with double dot colon ({@code :})
     */
    @Nullable
    private final String trailing;

    public boolean isActionMessage() {
        return trailing != null && trailing.matches("^\\001ACTION(.*)\\001$");
    }

    @Nullable
    public String getFormattedTrailing() {
        if (trailing != null) {
            return trailing.replace("\001ACTION", "").replace("\001", "");
        } else return null;
    }
}
