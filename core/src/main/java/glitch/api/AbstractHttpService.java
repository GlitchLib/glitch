package glitch.api;

import glitch.GlitchClient;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.auth.Scope;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Abstract HTTP Service provides {@link GlitchHttpClient HTTP Client} for the RESTful operations for:
 * <b>Twitch API v5</b> {@code glitch-kraken} and <b>New Twitch API</b> {@code glitch-helix}.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractHttpService {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Main Glitch Client
     * @return the Glitch Client
     */
    @Getter
    private final GlitchClient client;
    private final GlitchHttpClient http;

    /**
     * Create {@link HttpMethod#GET GET} Request
     * @param endpoint API Endpoint
     * @param responseType {@link Class} Response Type
     * @param <T> Response Type
     * @return HTTP Request which handled into {@link #exchange(HttpRequest)} method
     * @see #exchange(HttpRequest)
     */
    protected final <T> HttpRequest<T> get(String endpoint, Class<T> responseType) {
        return http.create(HttpMethod.GET, endpoint, responseType);
    }


    /**
     * Create {@link HttpMethod#POST POST} Request
     * @param endpoint API Endpoint
     * @param responseType {@link Class} Response Type
     * @param <T> Response Type
     * @return HTTP Request which handled into {@link #exchange(HttpRequest)} method
     * @see #exchange(HttpRequest)
     */
    protected final <T> HttpRequest<T> post(String endpoint, Class<T> responseType) {
        return http.create(HttpMethod.POST, endpoint, responseType);
    }


    /**
     * Create {@link HttpMethod#PUT PUT} Request
     * @param endpoint API Endpoint
     * @param responseType {@link Class} Response Type
     * @param <T> Response Type
     * @return HTTP Request which handled into {@link #exchange(HttpRequest)} method
     * @see #exchange(HttpRequest)
     */
    protected final <T> HttpRequest<T> put(String endpoint, Class<T> responseType) {
        return http.create(HttpMethod.PUT, endpoint, responseType);
    }


    /**
     * Create {@link HttpMethod#PATCH PATCH} Request
     * @param endpoint API Endpoint
     * @param responseType {@link Class} Response Type
     * @param <T> Response Type
     * @return HTTP Request which handled into {@link #exchange(HttpRequest)} method
     * @see #exchange(HttpRequest)
     */
    protected final <T> HttpRequest<T> patch(String endpoint, Class<T> responseType) {
        return http.create(HttpMethod.PATCH, endpoint, responseType);
    }


    /**
     * Create {@link HttpMethod#DELETE DELETE} Request
     * @param endpoint API Endpoint
     * @param responseType {@link Class} Response Type
     * @param <T> Response Type
     * @return HTTP Request which handled into {@link #exchange(HttpRequest)} method
     * @see #exchange(HttpRequest)
     */
    protected final <T> HttpRequest<T> delete(String endpoint, Class<T> responseType) {
        return http.create(HttpMethod.DELETE, endpoint, responseType);
    }

    /**
     * Exchange Request to Response
     * @param request HTTP Request
     * @param <T> Response Type
     * @return HTTP Response Handled into custom reactive response
     * @see #get(String, Class)
     * @see #post(String, Class)
     * @see #put(String, Class)
     * @see #patch(String, Class)
     * @see #delete(String, Class)
     */
    protected final <T> HttpResponse<T> exchange(HttpRequest<T> request) {
        return http.exchange(request);
    }

    protected final boolean checkRequiredScope(Collection<Scope> scopes, Scope requiredScope) {
        return scopes.contains(requiredScope);
    }
}
