package glitch.auth;

import glitch.GlitchClient;
import glitch.api.AbstractHttpService;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.auth.objects.json.Credential;
import glitch.auth.objects.json.impl.CredentialImpl;
import glitch.auth.objects.json.AccessToken;
import glitch.auth.objects.json.Validate;
import glitch.auth.objects.adapters.AccessTokenAdapter;
import glitch.auth.objects.adapters.ExpireInstantAdapter;
import glitch.auth.objects.adapters.ValidateAdapter;
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
        super(
                client,
                GlitchHttpClient.builder()
                        .withBaseUrl(BASE_URL)
                        .withDefaultTypeAdapters()
                        .addTypeAdapters(authAdapters())
                        .build()
        );
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
                .map(CredentialImpl::new)
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
                .map(CredentialImpl::new)
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
                .zipWith(Mono.just(userCredential), CredentialImpl::new)
                .cast(Credential.class)
                .flatMap(storage::register);
    }

    public AuthorizationUriBuilder buildAuthorizationUrl() {
        return new AuthorizationUriBuilder(BASE_URL, getClient().getConfiguration());
    }

    @SuppressWarnings("unchecked")
    private static Map<Type, Object> authAdapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        adapters.put(Instant.class, new ExpireInstantAdapter());
        adapters.put(AccessToken.class, new AccessTokenAdapter());
        adapters.put(Validate.class, new ValidateAdapter());

        return adapters;
    }
}
