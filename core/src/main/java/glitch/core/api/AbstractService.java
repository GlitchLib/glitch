package glitch.core.api;

import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.auth.Scope;
import glitch.core.utils.api.BaseURL;
import glitch.core.utils.api.HttpClient;

public abstract class AbstractService extends AbstractAPI {

    public AbstractService(GlitchClient client, HttpClient httpClient, BaseURL baseURL) {
        super(client, httpClient, baseURL);
    }

    protected boolean hasRequiredScope(Credential credential, Scope requiredScope) {
        return credential.getScopes().contains(requiredScope);
    }

    protected String authorization(String typeToken, Credential credential) {
        return String.format("%s %s", typeToken, credential.getAccessToken());
    }
}
