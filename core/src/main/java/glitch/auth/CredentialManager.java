package glitch.auth;

import glitch.GlitchClient;
import glitch.auth.json.Validate;
import glitch.core.utils.HttpUtils;
import io.reactivex.Single;

public final class CredentialManager {
    private final GlitchClient client;
    private final CredentialAPI api;

    public CredentialManager(GlitchClient client) {
        this.client = client;
        this.api = new CredentialAPI(client, HttpUtils.createForCredentials());
    }

    public Single<Credential> create(CredentialCreator creator) {
       return rebuild(creator);
    }

    final Single<Credential> rebuild(CredentialCreator credential) {
        Single<Validate> validate = api.validate(credential.getAccessToken());

        return validate.map(v -> new CredentialBuilder()
                .from(v)
                .accessToken(credential.getAccessToken())
                .refreshToken(credential.getRefreshToken())
                .build());
    }
}
