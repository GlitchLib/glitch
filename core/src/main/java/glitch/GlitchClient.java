package glitch;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import glitch.api.AbstractAPI;
import glitch.auth.Credential;
import glitch.auth.CredentialCreator;
import glitch.auth.CredentialManager;
import glitch.common.api.HttpClient;
import glitch.common.events.EventManager;
import io.reactivex.Single;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Wither;

@Getter
@RequiredArgsConstructor
public class GlitchClient {
    private final Config configuration;
    private final EventManager eventManager = new EventManager(this);
    private final CredentialManager credentialManager;
    private final ObjectMapper mapper = new ObjectMapper();


    public static Builder builder() {
        return new Builder();
    }

    @Setter
    @Getter
    @Accessors(chain = true, fluent = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private String clientId;
        private String clientSecret;
        private CredentialCreator botCredentials;

        private String userAgent = String.format("Glitch v%s [Rev. %s]", Property.VERSION, Property.REVISION);

        public Single<GlitchClient> build() {
            return Single.fromCallable(this::buildAsync);
        }

        public GlitchClient buildAsync() throws Exception {
            Config config = Config.from(this);

            return null;
        }
    }

    public <H extends AbstractAPI> HttpClient createClient(Class<H> api) {
        return null;
    }
}
