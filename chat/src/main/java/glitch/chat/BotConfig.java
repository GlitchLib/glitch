package glitch.chat;

import com.google.common.collect.ImmutableSet;
import glitch.auth.Credential;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.core.api.json.Badge;
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
                .badges(ImmutableSet.copyOf(botConfig.getBadges()))
                .from((Validate) credential)
                .from((AccessToken) credential)
                .build();
    }

    boolean isKnownBot();

    boolean isVerifiedBot();

    Color color();

    ImmutableSet<Badge> getBadges();

    @Value.Lazy
    default String getUsername() {
        return getLogin();
    }

    @Value.Lazy
    default String getPassword() {
        return "oauth:" + getAccessToken();
    }
}