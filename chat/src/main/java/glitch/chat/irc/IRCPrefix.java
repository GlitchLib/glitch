package glitch.chat.irc;

import glitch.utils.Immutable;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface IRCPrefix {
    String getRaw();
    @Nullable
    String getNick();
    @Nullable
    String getUser();
    String getHost();

    static IRCPrefix empty() {
        return new IRCPrefixBuilder().build();
    }
}
