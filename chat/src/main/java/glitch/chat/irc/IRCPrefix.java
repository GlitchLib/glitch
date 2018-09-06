package glitch.chat.irc;

import glitch.common.utils.Immutable;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface IRCPrefix {
    static IRCPrefix empty() {
        return new IRCPrefixBuilder().build();
    }

    String getRaw();

    @Nullable
    String getNick();

    @Nullable
    String getUser();

    String getHost();
}
