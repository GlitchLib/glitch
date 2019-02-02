package glitch;

import glitch.auth.CredentialManager;
import glitch.auth.GlitchScope;
import glitch.auth.store.EmptyStorage;
import glitch.auth.store.Storage;
import java.util.*;
import javax.annotation.Nullable;
import reactor.core.publisher.Mono;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
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

    public Config getConfiguration() {
        return configuration;
    }

    public CredentialManager getCredentialManager() {
        return credentialManager;
    }

    /**
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    public static class Builder {
        private final Set<GlitchScope> defaultScopes = new LinkedHashSet<>();
        private String clientId;
        private String clientSecret;
        @Nullable
        private String redirectUri;

        private String userAgent;

        private Storage storage = new EmptyStorage();

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder setRedirectUri(@Nullable String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Builder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder setStorage(Storage storage) {
            this.storage = storage;
            return this;
        }

        public Builder addDefaultScope(GlitchScope... scopes) {
            return addDefaultScopes(Arrays.asList(scopes));
        }

        public Builder addDefaultScopes(Collection<GlitchScope> scopes) {
            this.defaultScopes.addAll(scopes);
            return this;
        }

        public Mono<GlitchClient> buildAsync() {
            return Mono.just(build());
        }

        public GlitchClient build() {
            if (userAgent == null || userAgent.equals("")) {
                userAgent = String.format("Glitch v%s [Rev. %s]", GitProperty.get(GitProperty.APPLICATION_VERSION), GitProperty.get(GitProperty.GIT_COMMIT_ID_ABBREV));
            }

            Config config = new Config(
                    Objects.requireNonNull(this.clientId, "client_id == null"),
                    Objects.requireNonNull(this.clientSecret, "client_secret == null"),
                    this.userAgent,
                    this.defaultScopes, this.redirectUri
            );

            return new GlitchClient(config, storage);
        }
    }
}
