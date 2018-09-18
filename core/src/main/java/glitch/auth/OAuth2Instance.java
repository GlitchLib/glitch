package glitch.auth;

import feign.Target;

class OAuth2Instance extends Target.HardCodedTarget<CredentialAPI> {
    static final String BASE_URL = "https://id.twitch.tv/oauth2/";

    OAuth2Instance() {
        super(CredentialAPI.class, BASE_URL);
    }
}
