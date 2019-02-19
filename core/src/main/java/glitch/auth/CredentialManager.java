package glitch.auth;

import glitch.GlitchClient;
import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.api.http.Unofficial;
import glitch.auth.objects.adapters.AccessTokenAdapter;
import glitch.auth.objects.adapters.ValidateAdapter;
import glitch.auth.objects.json.AccessToken;
import glitch.auth.objects.json.Credential;
import glitch.auth.objects.json.Kraken;
import glitch.auth.objects.json.Validate;
import glitch.auth.store.Storage;
import glitch.service.AbstractHttpService;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import reactor.core.publisher.Mono;

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

    private static Map<Type, Object> authAdapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        adapters.put(AccessToken.class, new AccessTokenAdapter());
        adapters.put(Validate.class, new ValidateAdapter());

        return adapters;
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
        return http.exchangeAs(Routes.get("/validate").newRequest()
                .header("Authorization", "OAuth " + accessToken), Validate.class);
    }

    @Unofficial
    public Mono<Kraken> validFromKraken(Credential credential) {
        return validFromKraken(credential.getAccessToken());
    }

    @Unofficial
    public Mono<Kraken> validFromKraken(UserCredential credential) {
        return validFromKraken(credential.getAccessToken());
    }

    @Unofficial
    private Mono<Kraken> validFromKraken(String accessToken) {
        return http.exchangeAs(Routes.get("https://api.twitch.tv/kraken").newRequest()
                .header("Authorization", "OAuth " + accessToken)
                .header("Client-ID", getClient().getConfiguration().getClientId())
                .header("Accept", "application/vnd.twitchtv.v5+json"), KrakenToken.class)
                .map(k -> k.token);
    }

    public Mono<Credential> create(String code, String redirectUri) {
        return http.exchangeAs(
                Routes.post("/token")
                        .newRequest()
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", getClient().getConfiguration().getClientId())
                        .queryParam("client_secret", getClient().getConfiguration().getClientSecret())
                        .queryParam("code", code)
                        .queryParam("redirect_uri", Objects.requireNonNull((redirectUri != null) ? redirectUri : getClient().getConfiguration().getRedirectUri(), "redirect_uri == null")),
                AccessToken.class).zipWhen(this::valid, Credential::new)
                .cast(Credential.class)
                .doOnSuccess(credentialStorage::register);
    }

    public Mono<Credential> refresh(Credential credential) {
        return refresh(credential.getRefreshToken());
    }

    private Mono<Credential> refresh(String refreshToken) {
        return http.exchangeAs(
                Routes.post("/token")
                        .newRequest()
                        .queryParam("grant_type", "refresh_token")
                        .queryParam("client_id", getClient().getConfiguration().getClientId())
                        .queryParam("client_secret", getClient().getConfiguration().getClientSecret())
                        .queryParam("refresh", refreshToken),
                AccessToken.class).zipWhen(this::valid, Credential::new)
                .cast(Credential.class)
                .doOnSuccess(credentialStorage::register);
    }

    public Mono<Void> revoke(Credential credential) {
        return http.exchange(
                Routes.post("/revoke").newRequest()
                        .queryParam("client_id", getClient().getConfiguration().getClientId())
                        .queryParam("token", credential.getAccessToken())
        ).flatMap(r -> (r.isSuccessful()) ? Mono.<Void>empty() : Mono.error(handleErrorResponse(r)))
                .doOnSuccess(v -> credentialStorage.drop(credential));
    }

    public Mono<Credential> buildFromCredentials(UserCredential userCredential) {
        return validFromKraken(userCredential)
                .flatMap(kraken -> kraken.isValid() ?
                        valid(userCredential).map(valid -> new Credential(userCredential, valid, kraken)) :
                        refresh(userCredential.getRefreshToken())
                ).doOnSuccess(credentialStorage::register);
    }

    public AuthorizationUriBuilder buildAuthorizationUrl() {
        return new AuthorizationUriBuilder(BASE_URL, getClient().getConfiguration());
    }

    public Storage getCredentialStorage() {
        return credentialStorage;
    }

    private final class KrakenToken {
        private final Kraken token;

        private KrakenToken(Kraken token) {
            this.token = token;
        }

        public Kraken getToken() {
            return token;
        }
    }
}
