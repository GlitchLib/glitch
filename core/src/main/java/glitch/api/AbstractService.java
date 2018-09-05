package glitch.api;

import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.auth.Scope;
import glitch.common.api.BaseURL;
import glitch.common.api.HttpClient;

public abstract class AbstractService extends AbstractAPI {

    public AbstractService(GlitchClient client, HttpClient httpClient, BaseURL baseURL) {
        super(client, httpClient, baseURL);
    }

    protected boolean hasRequiredScope(Credential credential, Scope requiredScope) {
        return credential.getScopes().contains(requiredScope);
    }
}
