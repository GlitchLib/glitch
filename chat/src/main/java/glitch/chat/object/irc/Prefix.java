package glitch.chat.object.irc;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

/**
 * @author Damian Staszewski
 * @since 0.1.0
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Prefix {
    private final String raw;
    @Nullable
    private final String nick;
    @Nullable
    private final String user;
    private final String host;

    /**
     * @return
     */
    public static Prefix empty() {
        return fromRaw(":tmi.twitch.tv");
    }

    /**
     * @param rawPrefix
     * @return
     */
    public static Prefix fromRaw(String rawPrefix) {
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

        return new Prefix(rawPrefix, nick, user, host);
    }
}