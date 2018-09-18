package glitch;

import glitch.auth.CredentialManager;
import glitch.auth.Scope;
import glitch.auth.store.EmptyStorage;
import glitch.auth.store.Storage;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GlitchClient {
    private final Config configuration;
    private final CredentialManager credentialManager;

    private GlitchClient(Config config, Storage storage) {
        this.configuration = config;
        this.credentialManager = new CredentialManager(this, storage);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Setter
    @Getter
    @Accessors(chain = true, fluent = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private final Set<Scope> defaultScopes = new LinkedHashSet<>();
        private String clientId;
        private String clientSecret;
        @Nullable
        private String redirectUri;

        private String userAgent;

        private Storage storage = new EmptyStorage();

        public Set<Scope> defaultScopes() {
            return defaultScopes;
        }

        public Builder defaultScopes(Scope... scopes) {
            return defaultScopes(Arrays.asList(scopes));
        }

        public Builder defaultScopes(Collection<Scope> scopes) {
            defaultScopes.addAll(scopes);
            return this;
        }

        public Single<GlitchClient> buildAsync() {
            return Single.just(build());
        }

        public GlitchClient build() {
            Properties properties = Versions.getProperties();

            if (userAgent == null || userAgent.equals("")) {
                userAgent = String.format("Glitch v%s [Rev. %s]", properties.getProperty(Versions.APPLICATION_VERSION), properties.getProperty(Versions.GIT_COMMIT_ID_ABBREV));
            }

            Config config = ConfigImpl.builder()
                    .clientId(Objects.requireNonNull(this.clientId))
                    .clientSecret(Objects.requireNonNull(this.clientSecret))
                    .userAgent(this.userAgent)
                    .defaultScopes(this.defaultScopes)
                    .redirectUri(this.redirectUri)
                    .build();

            return new GlitchClient(config, storage);
        }
    }
}
