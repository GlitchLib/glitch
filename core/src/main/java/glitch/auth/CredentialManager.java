package glitch.auth;

import glitch.GlitchClient;
import glitch.api.AbstractHttpService;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.auth.objects.AccessToken;
import glitch.auth.objects.Validate;
import glitch.auth.objects.converters.AccessTokenAdapter;
import glitch.auth.objects.converters.ExpireInstantAdapter;
import glitch.auth.objects.converters.ValidateAdapter;
import glitch.auth.store.Storage;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CredentialManager extends AbstractHttpService {
    private static final String BASE_URL = "https://id.twitch.tv/oauth2";

    private final Storage storage;

    public CredentialManager(GlitchClient client, Storage storage) {
        super(client, GlitchHttpClient.builder().withBaseUrl(BASE_URL).build());
        this.storage = storage;
    }

    public Mono<Validate> valid(Credential credential) {
        return valid(credential.getAccessToken());
    }

    public Mono<Validate> valid(UserCredential credential) {
        return valid(credential.getAccessToken());
    }

    private Mono<Validate> valid(AccessToken at) {
        return valid(at.getAccessToken());
    }

    private Mono<Validate> valid(String accessToken) {
        return exchange(get("/validate", Validate.class)
                .header("Authorization", "OAuth " + accessToken))
                .toMono();
    }

    public Mono<Credential> create(String code, String redirectUri) {
        HttpRequest<AccessToken> accessTokenRequest = post("/token", AccessToken.class)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", getClient().getConfiguration().getClientId())
                .queryParam("client_secret", getClient().getConfiguration().getClientSecret())
                .queryParam("code", code)
                .queryParam("redirect_uri", Objects.requireNonNull((redirectUri != null) ? redirectUri : getClient().getConfiguration().getRedirectUri(), "redirect_uri == null"));

        return exchange(accessTokenRequest).toMono()
                .zipWhen(this::valid)
                .map(t -> new CredentialImpl(t.getT1(), t.getT2()))
                .cast(Credential.class)
                .flatMap(storage::register);
    }

    public Mono<Credential> refresh(Credential credential) {
        HttpRequest<AccessToken> accessTokenRequest = post("/token", AccessToken.class)
                .queryParam("grant_type", "refresh_token")
                .queryParam("client_id", getClient().getConfiguration().getClientId())
                .queryParam("client_secret", getClient().getConfiguration().getClientSecret())
                .queryParam("refresh", credential.getRefreshToken());

        return exchange(accessTokenRequest).toMono()
                .zipWhen(this::valid)
                .map(t -> new CredentialImpl(t.getT1(), t.getT2()))
                .cast(Credential.class)
                .flatMap(storage::register);
    }

    public Mono<Void> revoke(Credential credential) {
        HttpRequest<Void> revoke = post("/revoke", Void.class)
                .queryParam("client_id", getClient().getConfiguration().getClientId())
                .queryParam("token", credential.getAccessToken());

        return exchange(revoke).toMono().then(storage.drop(credential));
    }

    public Mono<Credential> buildFromCredentials(UserCredential userCredential) {
        return valid(userCredential)
                .map(validate -> new CredentialImpl(userCredential, validate))
                .cast(Credential.class)
                .flatMap(storage::register);
    }

    public AuthorizationUriBuilder buildAuthorization() {
        return new AuthorizationUriBuilder(BASE_URL, getClient().getConfiguration());
    }

    @SuppressWarnings("unchecked")
    private static Map<Type, Object> adapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        adapters.put(Instant.class, new ExpireInstantAdapter());
        adapters.put(AccessToken.class, new AccessTokenAdapter());
        adapters.put(Validate.class, new ValidateAdapter());

        return adapters;
    }
}
