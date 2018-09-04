package glitch;

import glitch.chat.BotConfig;
import glitch.common.utils.Immutable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Config {

    String getClientId();
    String getClientSecret();
    BotConfig getBotCredentials();
    String getUserAgent();

    static Config from(GlitchClient.Builder builder) {
        return null;
    }
}
