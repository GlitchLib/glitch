package glitch.auth;

import glitch.Config;
import glitch.common.api.BaseURL;
import glitch.common.api.HttpClient;
import glitch.common.api.HttpMethod;
import glitch.common.api.Router;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CredentialAPI {
    private final Config config;
    private final HttpClient httpClient;
    private final BaseURL baseURL = BaseURL.create("https://id.twitch.tv/oauth2");

    public Validate validate(Credential credential) throws Exception {
        return Router.<Validate>create(HttpMethod.GET, baseURL.endpoint("/validate"))
                .request()
                .header("Authorization", "OAuth " + credential.getAccessToken())
                .exchange(httpClient);
    }

    public Credential create(String code, String redirectUri) throws Exception {
        AccessToken token = Router.<AccessToken>create(HttpMethod.POST, baseURL.endpoint("/token"))
                .request()
                .exchange(httpClient);

        CredentialBuilder cb = new CredentialBuilder().from(token);

        Validate validate = validate(cb.build());

        return cb.from(validate).build();
    }

    public Credential refresh(Credential credential) throws Exception {
        AccessToken token = Router.<AccessToken>create(HttpMethod.POST, baseURL.endpoint("/token"))
                .request()
                .exchange(httpClient);

        CredentialBuilder cb = new CredentialBuilder().from(token);

        Validate validate = validate(cb.build());

        return cb.from(validate).build();
    }

    public void revoke(Credential credential) throws Exception {
        Router.<Void>create(HttpMethod.POST, baseURL.endpoint("revoke"))
                .request()
                .exchange(httpClient);
    }
}
