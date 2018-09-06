package glitch;

import glitch.api.AbstractAPI;
import glitch.auth.CredentialManager;
import glitch.auth.Scope;
import glitch.common.api.HttpClient;
import glitch.common.events.EventManager;
import glitch.common.utils.HttpUtils;
import io.reactivex.Single;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
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
        return HttpUtils.createForApi(configuration, api.getSimpleName().contains("Kraken"));
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

        private String userAgent = String.format("Glitch v%s [Rev. %s]", Property.VERSION, Property.REVISION);

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
            return new GlitchClient(Config.from(this));
        }
    }
}
