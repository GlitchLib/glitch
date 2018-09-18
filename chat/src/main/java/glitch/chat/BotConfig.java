package glitch.chat;

import glitch.auth.Credential;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.core.utils.Immutable;
import java.awt.Color;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters
public interface BotConfig extends Credential {
    static BotConfig from(Credential credential, GlitchChat.BotConfigInfo botConfig) {
        return BotConfigImpl.builder()
                .isKnownBot(botConfig.isKnownBot())
                .isVerifiedBot(botConfig.isVerifiedBot())
                .color(botConfig.getColor())
                .from((Validate) credential)
                .from((AccessToken) credential)
                .build();
    }

    boolean isKnownBot();

    boolean isVerifiedBot();

    Color color();

    @Value.Lazy
    default String getUsername() {
        return getLogin();
    }

    @Value.Lazy
    default String getPassword() {
        return "oauth:" + getAccessToken();
    }
}