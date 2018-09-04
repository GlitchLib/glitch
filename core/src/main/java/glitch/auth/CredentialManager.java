package glitch.auth;

import glitch.GlitchClient;
import glitch.auth.json.Validate;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CredentialManager {
    private final GlitchClient client;
    private final CredentialAPI api;

    public Credential create(CredentialCreator creator) throws Exception {
        CredentialBuilder cb = new CredentialBuilder()
                .accessToken(creator.getAccessToken())
                .refreshToken(creator.getRefreshToken());

        Validate validate = api.validate(cb.build());

        return cb.from(validate).build();
    }

    final Single<Credential> rebuild(Credential credential) {
        return Single.create(sub -> sub.onSuccess(rebuildAsync(credential)));
    }

    final Credential rebuildAsync(Credential credential) throws Exception {
        Validate validate = api.validate(credential);

        return new CredentialBuilder().from(credential).from(validate).build();
    }
}
