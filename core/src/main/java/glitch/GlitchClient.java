package glitch;

import glitch.api.AbstractRestService;
import glitch.auth.CredentialManager;
import glitch.auth.Scope;
import glitch.auth.store.EmptyStorage;
import glitch.auth.store.Storage;
import java.util.*;
import javax.annotation.Nullable;
import lombok.*;
import lombok.experimental.Accessors;
import reactor.core.publisher.Mono;

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

    @Data
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

        public Builder defaultScopes(Scope... scopes) {
            return defaultScopes(Arrays.asList(scopes));
        }

        public Builder defaultScopes(Collection<Scope> scopes) {
            defaultScopes.addAll(scopes);
            return this;
        }

        public Mono<GlitchClient> buildAsync() {
            return Mono.just(build());
        }

        public GlitchClient build() {
            Properties properties = Versions.getProperties();

            if (userAgent == null || userAgent.equals("")) {
                userAgent = String.format("Glitch v%s [Rev. %s]", properties.getProperty(Versions.APPLICATION_VERSION), properties.getProperty(Versions.GIT_COMMIT_ID_ABBREV));
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
