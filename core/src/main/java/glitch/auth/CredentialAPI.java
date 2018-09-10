package glitch.auth;

import glitch.GlitchClient;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.core.utils.api.BaseURL;
import glitch.core.utils.api.HttpClient;
import glitch.core.utils.api.HttpMethod;
import glitch.core.utils.api.Router;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CredentialAPI {
    private final GlitchClient client;
    private final HttpClient httpClient;
    private final BaseURL baseURL = BaseURL.create("https://id.twitch.tv/oauth2");

    public Validate validate(Credential credential) throws Exception {
        return validate(credential.getAccessToken());
    }

    Validate validate(String accessToken) throws Exception {
        return Router.<Validate>create(HttpMethod.GET, baseURL.endpoint("/validate"))
                .request()
                .header("Authorization", "OAuth " + accessToken)
                .exchange(httpClient);
    }

    public Credential create(String code, @Nullable String redirectUri) throws Exception {
        AccessToken token = Router.<AccessToken>create(HttpMethod.POST, baseURL.endpoint("/token"))
                .request()
                .queryParam("client_id", client.getConfiguration().getClientId())
                .queryParam("client_secret", client.getConfiguration().getClientSecret())
                .queryParam("grant_type", "authorization_code")
                .queryParam("code", code)
                .queryParam("redirect_uri", Objects.requireNonNull((redirectUri != null) ? redirectUri : client.getConfiguration().getRedirectUri(), "redirect_uri"))
                .exchange(httpClient);

        CredentialBuilder cb = new CredentialBuilder().from(token);

        Validate validate = validate(cb.build());

        return cb.from(validate).build();
    }

    public Credential refresh(Credential credential) throws Exception {
        AccessToken token = Router.<AccessToken>create(HttpMethod.POST, baseURL.endpoint("/token"))
                .request()
                .queryParam("client_id", client.getConfiguration().getClientId())
                .queryParam("client_secret", client.getConfiguration().getClientSecret())
                .queryParam("grant_type", "refresh_token")
                .queryParam("refresh_token", credential.getRefreshToken())
                .exchange(httpClient);

        CredentialBuilder cb = new CredentialBuilder().from(token);

        Validate validate = validate(cb.build());

        return cb.from(validate).build();
    }

    public void revoke(Credential credential) throws Exception {
        Router.<Void>create(HttpMethod.POST, baseURL.endpoint("/revoke"))
                .request()
                .queryParam("client_id", credential.getClientId())
                .queryParam("token", credential.getAccessToken())
                .exchange(httpClient);
    }
}
