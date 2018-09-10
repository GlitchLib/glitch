package glitch;

import glitch.core.api.AbstractAPI;
import glitch.auth.CredentialManager;
import glitch.auth.Scope;
import glitch.core.utils.api.HttpClient;
import glitch.core.events.EventManager;
import glitch.core.utils.HttpUtils;
import io.reactivex.Single;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
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
    private final EventManager eventManager = new EventManager(this);
    private final CredentialManager credentialManager = new CredentialManager(this);

    public static Builder builder() {
        return new Builder();
    }

    public <H extends AbstractAPI> HttpClient createClient(Class<H> api) {
        return HttpUtils.createForApi(configuration, api.getSimpleName().equals("KrakenAPI"));
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

        public Single<GlitchClient> build() {
            return Single.fromCallable(this::buildAsync);
        }

        public GlitchClient buildAsync() throws Exception {
            Properties properties = Versions.getProperties();

            if (userAgent == null || userAgent.equals("")) {
                userAgent = String.format("Glitch v%s [Rev. %s]", properties.getProperty(Versions.APPLICATION_VERSION), properties.getProperty(Versions.GIT_COMMIT_ID_ABBREV));
            }

            return new GlitchClient(Config.from(this));
        }
    }
}
