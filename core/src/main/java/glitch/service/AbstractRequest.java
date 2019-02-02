package glitch.service;

import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.GlitchScope;
import glitch.exceptions.GlitchEncodeException;
import glitch.exceptions.http.ResponseException;
import glitch.exceptions.http.ScopeIsMissingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNullApi;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class AbstractRequest<SINGLE, MULTI extends OrdinalList<SINGLE>> {

    protected final HttpClient httpClient;
    protected final HttpRequest request;

    /**
     * Creates instance of Abstract Request
     * @param httpClient HTTP Client
     * @param request Request
     */
    protected AbstractRequest(HttpClient httpClient, HttpRequest request) {
        this.httpClient = httpClient;
        this.request = request;
    }

    @Nonnull
    public abstract Mono<MULTI> get();

    @SuppressWarnings("unchecked")
    private Class<MULTI> getParameterizedType() {
        return (Class<MULTI>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void async(Consumer<MULTI> response, Consumer<Throwable> exceptions) {
        get().subscribe(response, exceptions);
    }

    public void async(Consumer<MULTI> response) {
        get().subscribe(response);
    }

    public Flux<SINGLE> getMany() {
        return get().flatMapIterable(OrdinalList::getData);
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
     * Handling {@link glitch.exceptions.http.ScopeIsMissingException}
     *
     * @param requiredScope required {@link glitch.auth.GlitchScope}
     * @return throwable exception called {@link glitch.exceptions.http.ScopeIsMissingException}
     */
    protected ScopeIsMissingException handleScopeMissing(GlitchScope requiredScope) {
        return new ScopeIsMissingException(requiredScope);
    }

    protected static String encodeQuery(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GlitchEncodeException(e);
        }
    }
}
