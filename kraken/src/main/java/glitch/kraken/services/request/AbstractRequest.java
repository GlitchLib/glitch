package glitch.kraken.services.request;

import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.auth.Scope;
import glitch.exceptions.http.ScopeIsMissingException;
import lombok.Data;

import java.util.Collection;

@Data
public abstract class AbstractRequest<R> {
    protected final GlitchHttpClient httpClient;
    protected final HttpRequest<R> request;

    protected abstract HttpResponse<R> exchange();

    /**
     * Checks Required scopes
     *
     * @param scopes        list of {@link Scope}
     * @param requiredScope required {@link Scope}
     * @return scope is exist
     */
    protected final boolean checkRequiredScope(Collection<Scope> scopes, Scope requiredScope) {
        return scopes.contains(requiredScope);
    }

    /**
     * Handling {@link ScopeIsMissingException}
     *
     * @param requiredScope required {@link Scope}
     * @return throwable exception called {@link ScopeIsMissingException}
     */
    protected ScopeIsMissingException handleScopeMissing(Scope requiredScope) {
        return new ScopeIsMissingException(requiredScope);
    }
}
