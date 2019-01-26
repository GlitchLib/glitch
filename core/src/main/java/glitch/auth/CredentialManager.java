package glitch.auth;

import glitch.GlitchClient;
import glitch.api.http.Routes;
import glitch.service.AbstractHttpService;
import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.auth.objects.adapters.AccessTokenAdapter;
import glitch.auth.objects.adapters.ExpireInstantAdapter;
import glitch.auth.objects.adapters.ValidateAdapter;
import glitch.auth.objects.json.AccessToken;
import glitch.auth.objects.json.Credential;
import glitch.auth.objects.json.Validate;
import glitch.auth.objects.json.impl.CredentialImpl;
import glitch.auth.store.Storage;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CredentialManager extends AbstractHttpService {
    private static final String BASE_URL = "https://id.twitch.tv/oauth2";

    private final Storage credentialStorage;

    public CredentialManager(GlitchClient client, Storage credentialStorage) {
        super(
                client,
                HttpClient.builder()
                        .withBaseUrl(BASE_URL)
                        .withDefaultTypeAdapters()
                        .addTypeAdapters(authAdapters())
                        .build()
        );
        this.credentialStorage = credentialStorage;
    }

    public CompletableFuture<Validate> valid(Credential credential) {
        return valid(credential.getAccessToken());
    }

    public CompletableFuture<Validate> valid(UserCredential credential) {
        return valid(credential.getAccessToken());
    }

    private CompletableFuture<Validate> valid(AccessToken at) {
        return valid(at.getAccessToken());
    }

    private CompletableFuture<Validate> valid(String accessToken) {
        return complete(Routes.get("/validate", Validate.class).newRequest()
                .header("Authorization", "OAuth " + accessToken));
    }

    public CompletableFuture<Credential> create(String code, String redirectUri) {
        return complete(
                Routes.post("/token", AccessToken.class)
                        .newRequest()
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", getClient().getConfiguration().getClientId())
                        .queryParam("client_secret", getClient().getConfiguration().getClientSecret())
                        .queryParam("code", code)
                        .queryParam("redirect_uri", Objects.requireNonNull((redirectUri != null) ? redirectUri : getClient().getConfiguration().getRedirectUri(), "redirect_uri == null"))
        ).thenComposeAsync(token -> this.valid(token).thenApply(validate -> (Credential) new CredentialImpl(token, validate)))
                .whenComplete((c, ex) -> credentialStorage.register(c));
    }

    public CompletableFuture<Credential> refresh(Credential credential) {
        return complete(
                Routes.post("/token", AccessToken.class)
                        .newRequest()
                        .queryParam("grant_type", "refresh_token")
                        .queryParam("client_id", getClient().getConfiguration().getClientId())
                        .queryParam("client_secret", getClient().getConfiguration().getClientSecret())
                        .queryParam("refresh", credential.getRefreshToken())
        ).thenComposeAsync(token -> this.valid(token).thenApply(validate -> (Credential) new CredentialImpl(token, validate)))
                .whenComplete((c, ex) -> credentialStorage.register(c));
    }

    public CompletableFuture<Void> revoke(Credential credential) {
        return complete(
                Routes.post("/revoke", Void.class).newRequest()
                        .queryParam("client_id", getClient().getConfiguration().getClientId())
                        .queryParam("token", credential.getAccessToken())
        ).whenComplete(($, ex) -> credentialStorage.drop(credential));
    }

    public CompletableFuture<Credential> buildFromCredentials(UserCredential userCredential) {
        return valid(userCredential)
                .thenApply(validate -> (Credential) new CredentialImpl(validate, userCredential))
                .whenComplete((c, ex) -> credentialStorage.register(c));
    }

    public AuthorizationUriBuilder buildAuthorizationUrl() {
        return new AuthorizationUriBuilder(BASE_URL, getClient().getConfiguration());
    }

    public Storage getCredentialStorage() {
        return credentialStorage;
    }

    private static Map<Type, Object> authAdapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        adapters.put(Instant.class, new ExpireInstantAdapter());
        adapters.put(AccessToken.class, new AccessTokenAdapter());
        adapters.put(Validate.class, new ValidateAdapter());

        return adapters;
    }
}
