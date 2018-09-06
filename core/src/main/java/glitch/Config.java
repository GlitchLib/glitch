package glitch;

import glitch.auth.Scope;
import glitch.common.utils.Immutable;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Config {
    static Config from(GlitchClient.Builder builder) {
        return new ConfigBuilder()
                .clientId(Objects.requireNonNull(builder.clientId()))
                .clientSecret(Objects.requireNonNull(builder.clientSecret()))
                .userAgent(builder.userAgent())
                .defaultScopes(builder.defaultScopes())
                .redirectUri(builder.redirectUri())
                .build();
    }

    String getClientId();

    String getClientSecret();

    String getUserAgent();

    Set<Scope> getDefaultScopes();

    @Nullable
    String getRedirectUri();
}
