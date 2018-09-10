package glitch.chat;

import glitch.auth.Credential;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.chat.json.BotInfo;
import glitch.core.utils.Immutable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface BotConfig extends Credential, BotInfo {
    static BotConfig from(Credential credential, BotInfo botInfo) {
        return new BotConfigBuilder()
                .from((Validate) credential)
                .from((AccessToken) credential)
                .build();
    }

    @Value.Lazy
    default String getUsername() {
        return getLogin();
    }

    @Value.Lazy
    default String getPassword() {
        return "oauth:" + getAccessToken();
    }
}