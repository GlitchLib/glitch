package glitch.auth;

import glitch.auth.json.Validate;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CredentialManager {
    private final CredentialAPI api;

    final Single<Credential> rebuild(Credential credential) {
        return Single.create(sub -> sub.onSuccess(rebuildAsync(credential)));
    }

    final Credential rebuildAsync(Credential credential) throws Exception {
        Validate validate = api.validate(credential);

        return new CredentialBuilder().from(credential).from(validate).build();
    }
}
