package glitch.core.api;

import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.auth.Scope;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractService<R> {
    protected final GlitchClient client;
    protected final R api;

    protected boolean hasRequiredScope(Credential credential, Scope requiredScope) {
        return credential.getScopes().contains(requiredScope);
    }
}
