package glitch.service;

import glitch.GlitchClient;
import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.auth.GlitchScope;
import glitch.exceptions.http.ResponseException;
import glitch.exceptions.http.ScopeIsMissingException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Abstract HTTP Service provides {@link glitch.api.http.HttpClient HTTP Client} for the RESTful operations for:
 * <b>Twitch API v5</b> {@code glitch-kraken} and <b>New Twitch API</b> {@code glitch-helix}.
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class AbstractHttpService implements IService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final HttpClient http;
    private final GlitchClient client;

    /**
     * Constructor of Abstract HTTP Service
     *
     * @param client the {@link glitch.GlitchClient}
     * @param http   initialized {@link glitch.api.http.HttpClient}
     */
    protected AbstractHttpService(GlitchClient client, HttpClient http) {
        this.client = client;
        this.http = http;
    }

    protected <T> Mono<T> exchangeTo(HttpRequest request, Class<T> type) {
        return http.exchangeAs(request, type);
    }

    protected Mono<HttpResponse> exchange(HttpRequest request) {
        return http.exchange(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlitchClient getClient() {
        return client;
    }

    protected final ResponseException handleErrorResponse(HttpResponse response) {
        return response.getBodyAs(ResponseException.class);
    }

    /**
     * Checks Required scopes
     *
     * @param scopes        list of {@link glitch.auth.GlitchScope}
     * @param requiredScope required {@link glitch.auth.GlitchScope}
     * @return scope is exist
     */
    protected final boolean checkRequiredScope(Collection<GlitchScope> scopes, GlitchScope requiredScope) {
        return scopes.contains(requiredScope);
    }

    /**
     * Handling {@link ScopeIsMissingException}
     *
     * @param requiredScope required {@link glitch.auth.GlitchScope}
     * @return throwable exception called {@link ScopeIsMissingException}
     */
    protected ScopeIsMissingException handleScopeMissing(GlitchScope requiredScope) {
        return new ScopeIsMissingException(requiredScope);
    }


}
