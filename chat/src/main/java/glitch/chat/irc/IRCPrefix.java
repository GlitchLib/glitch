package glitch.chat.irc;

import glitch.socket.utils.EventImmutable;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface IRCPrefix {
    static IRCPrefix empty() {
        return IRCPrefixImpl.of("", null, null, "");
    }

    static IRCPrefix fromRaw(String rawPrefix) {
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

        return IRCPrefixImpl.of(rawPrefix, nick, user, host);
    }

    String getRaw();

    @Nullable
    String getNick();

    @Nullable
    String getUser();

    String getHost();
}
