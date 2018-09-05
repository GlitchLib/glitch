package glitch.chat;

import glitch.auth.Credential;
import glitch.utils.Immutable;
import java.awt.Color;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface BotConfig extends Credential {
    @Value.Lazy
    default String getUsername() {
        return getLogin();
    }

    @Value.Lazy
    default String getPassword() {
        return "oauth:" + getAccessToken();
    }

    boolean isKnownBot();
    boolean isVerifiedBot();
    Color color();
}