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

    public Credential create(CredentialCreator creator) throws Exception {
        CredentialBuilder cb = new CredentialBuilder()
                .accessToken(creator.getAccessToken())
                .refreshToken(creator.getRefreshToken());

        Validate validate = api.validate(cb.build());

        return cb.from(validate).build();
    }

    final Single<Credential> rebuild(CredentialCreator credential) {
        return Single.create(sub -> sub.onSuccess(rebuildAsync(credential)));
    }

    final Credential rebuildAsync(CredentialCreator credential) throws Exception {
        Validate validate = api.validate(credential.getAccessToken());

        return new CredentialBuilder()
                .from(validate)
                .accessToken(credential.getAccessToken())
                .refreshToken(credential.getRefreshToken())
                .build();
    }
}
