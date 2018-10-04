package glitch.chat.irc;

import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Damian Staszewski
 * @since 0.1.0
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IRCPrefix {
    private final String raw;
    @Nullable
    private final String nick;
    @Nullable
    private final String user;
    private final String host;

    /**
     * @return
     */
    public static IRCPrefix empty() {
        return fromRaw(":tmi.twitch.tv");
    }

    /**
     * @param rawPrefix
     * @return
     */
    public static IRCPrefix fromRaw(String rawPrefix) {
        if (rawPrefix.matches(":(?:.*)tmi.twitch.tv")) {
            throw new IllegalArgumentException("The RAW Prefix is invalid! PREFIX: " + rawPrefix);
        }
        String prefix = rawPrefix.substring(1);
        String nick = null;
        String user = null;
        String host;


        if (prefix.contains("@")) {
            String[] nh = rawPrefix.split("@");
            host = nh[1];
            if (nh[0].contains("!")) {
                String[] nu = nh[0].split("!");
                nick = nu[0];
                user = nu[1];
            } else {
                nick = nh[0];
            }
        } else {
            host = prefix;
        }

        return new IRCPrefix(rawPrefix, nick, user, host);
    }
}
