package glitch.service;

import glitch.GlitchClient;
import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.auth.GlitchScope;
import glitch.exceptions.http.ResponseException;
import glitch.exceptions.http.ScopeIsMissingException;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract HTTP Service provides {@link glitch.api.http.HttpClient HTTP Client} for the RESTful operations for:
 * <b>Twitch API v5</b> {@code glitch-kraken} and <b>New Twitch API</b> {@code glitch-helix}.
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class AbstractHttpService implements IService {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final GlitchClient client;
    protected final HttpClient http;

    /**
     * Constructor of Abstract HTTP Service
     * @param client the {@link glitch.GlitchClient}
     * @param http initialized {@link glitch.api.http.HttpClient}
     */
    protected AbstractHttpService(GlitchClient client, HttpClient http) {
        this.client = client;
        this.http = http;
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
