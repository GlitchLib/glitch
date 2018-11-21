package glitch.api.http;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

/**
 * HTTP Request created by {@code {@link GlitchHttpClient#create(HttpMethod, String, Class)}}.
 * This is a ordinal Request Builder.
 * @param <T> Response Type
 */
public class HttpRequest<T> {
    final HttpMethod method;
    final String endpoint;
    final Class<T> responseType;

    @Nullable
    Object body;

    final Multimap<String, String> headers = HashMultimap.create();
    final Multimap<String, ? extends Serializable> queryParams = HashMultimap.create();

    HttpRequest(HttpMethod method, String endpoint, Class<T> responseType) {
        this.method = method;
        this.endpoint = endpoint;
        this.responseType = responseType;
    }

    /**
     * Set Request Body
     * @param body body Object
     * @return this
     */
    public HttpRequest<T> body(Object body) {
        this.body = body;
        return this;
    }

    /**
     * Add Header to the Request
     * @param key Header Key
     * @param value Header Value
     * @return this
     */
    public HttpRequest<T> header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * Add Headers to the Request
     * @param headers {@link Map} of Headers
     * @return this
     */
    public HttpRequest<T> headers(Map<String, String> headers) {
        headers.forEach(this::header);
        return this;
    }

    /**     *
     * Add Query Param to the Request
     * @param key param key
     * @param value param value
     * @param <S> Serializable value
     * @return this
     */
    public <S extends Serializable> HttpRequest<T> queryParam(String key, S value) {
        return this;
    }

    /**
     * Add Query Params to the Request
     * @param queryParams {@link Map} of Query Params
     * @return this
     */
    public <S extends Serializable> HttpRequest<T> queryParams(Map<String, S> queryParams) {
        queryParams.forEach(this::queryParam);
        return this;
    }
}
