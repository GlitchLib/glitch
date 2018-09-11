package glitch.auth;

import glitch.GlitchClient;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.core.utils.api.BaseURL;
import glitch.core.utils.api.HttpClient;
import glitch.core.utils.api.HttpMethod;
import glitch.core.utils.api.Router;
import io.reactivex.Single;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CredentialAPI {
    private final GlitchClient client;
    private final HttpClient httpClient;
    private final BaseURL baseURL = BaseURL.create("https://id.twitch.tv/oauth2");

    public Single<Validate> validate(Credential credential) {
        return validate(credential.getAccessToken());
    }

    Single<Validate> validate(String accessToken) {
        return Router.create(HttpMethod.GET, baseURL.endpoint("/validate"), Validate.class)
                .request()
                .header("Authorization", "OAuth " + accessToken)
                .exchange(httpClient);
    }

    public Single<Credential> create(String code, @Nullable String redirectUri) throws Exception {
        Single<AccessToken> token = Router.create(HttpMethod.POST, baseURL.endpoint("/token"), AccessToken.class)
                .request()
                .queryParam("client_id", client.getConfiguration().getClientId())
                .queryParam("client_secret", client.getConfiguration().getClientSecret())
                .queryParam("grant_type", "authorization_code")
                .queryParam("code", code)
                .queryParam("redirect_uri", Objects.requireNonNull((redirectUri != null) ? redirectUri : client.getConfiguration().getRedirectUri(), "redirect_uri"))
                .exchange(httpClient);

        Single<CredentialBuilder> cb = token.map(new CredentialBuilder()::from);

        Single<Validate> validate = cb.flatMap(b -> validate(b.build()));

        return cb.zipWith(validate, (b, v) -> b.from(v).build());
    }

    public Single<Credential> refresh(Credential credential) {
        Single<AccessToken> token = Router.create(HttpMethod.POST, baseURL.endpoint("/token"), AccessToken.class)
                .request()
                .queryParam("client_id", client.getConfiguration().getClientId())
                .queryParam("client_secret", client.getConfiguration().getClientSecret())
                .queryParam("grant_type", "refresh_token")
                .queryParam("refresh_token", credential.getRefreshToken())
                .exchange(httpClient);


        Single<CredentialBuilder> cb = token.map(new CredentialBuilder()::from);

        Single<Validate> validate = cb.flatMap(b -> validate(b.build()));

        return cb.zipWith(validate, (b, v) -> b.from(v).build());
    }

    public Single<Void> revoke(Credential credential) {
        return Router.create(HttpMethod.POST, baseURL.endpoint("/revoke"), Void.class)
                .request()
                .queryParam("client_id", credential.getClientId())
                .queryParam("token", credential.getAccessToken())
                .exchange(httpClient);
    }
}
