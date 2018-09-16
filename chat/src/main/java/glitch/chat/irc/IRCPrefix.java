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

    String getRaw();

    @Nullable
    String getNick();

    @Nullable
    String getUser();

    String getHost();
}
