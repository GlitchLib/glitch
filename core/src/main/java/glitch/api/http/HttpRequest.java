package glitch.api.http;

import com.google.gson.Gson;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import kotlin.text.Charsets;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;

/**
 * HTTP Request created by {@code {@link HttpClient#create(HttpMethod, String, Class)}}.
 * This is a ordinal Request Builder.
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class HttpRequest extends Object {
    final HttpMethod method;
    final String endpoint;
    final MultiValuedMap<String, String> headers = MultiMapUtils.newSetValuedHashMap();
    final MultiValuedMap<String, Object> queryParams = MultiMapUtils.newSetValuedHashMap();
    @Nullable
    Object body;
    BodyType bodyType;
    boolean serializeNullsBody = false;

    HttpRequest(HttpMethod method, String endpoint) {
        this.method = method;
        this.endpoint = endpoint;
    }

    /**
     * Set Request Body
     *
     * @param body           body Object
     * @param serializeNulls serialize nulls
     * @return this
     */
    public HttpRequest body(BodyType bodyType, Object body, boolean serializeNulls) {
        this.bodyType = bodyType;
        this.body = body;
        this.serializeNullsBody = serializeNulls;
        return this;
    }

    /**
     * Set Request Body without serializing nulls
     *
     * @param body body Object
     * @return this
     */
    public HttpRequest body(BodyType bodyType, Object body) {
        return body(bodyType, body, false);
    }

    /**
     * Add Header to the Request
     *
     * @param key   Header Key
     * @param value Header Value
     * @return this
     */
    public HttpRequest header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * Add Headers to the Request
     *
     * @param headers {@link Map} of Headers
     * @return this
     */
    public HttpRequest headers(Map<String, String> headers) {
        headers.forEach(this::header);
        return this;
    }

    /**
     * Add Query Param to the Request
     *
     * @param key   param key
     * @param value param value
     * @param <S>   Serializable value
     * @return this
     */
    public HttpRequest queryParam(String key, Object value) {
        this.queryParams.put(key, value);
        return this;
    }

    /**
     * Add Query Params to the Request
     *
     * @param queryParams {@link Map} of Query Params
     * @return this
     */
    public HttpRequest queryParams(MultiValuedMap<String, Object> queryParams) {
        this.queryParams.putAll(queryParams);
        return this;
    }

    /**
     * Add Query Params to the Request
     *
     * @param queryParams {@link Map} of Query Params
     * @return this
     */
    public HttpRequest queryParams(Map<String, Object> queryParams) {
        this.queryParams.putAll(queryParams);
        return this;
    }

    public enum BodyType {
        JSON("application/json", (data, gson) -> gson.toJson(data).getBytes(Charsets.UTF_8)),
        FORM("application/x-www-form-urlencoded", (data, gson) ->
                gson.toJsonTree(data).getAsJsonObject().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&")).getBytes(Charsets.UTF_8)),
        FILE("multipart/form-data", (data, gson) -> {
            try {
                if (data instanceof File) {
                    return Files.readAllBytes(((File) data).toPath());
                } else if (data instanceof Path) {
                    return Files.readAllBytes((Path) data);
                } else if (data instanceof String) {
                    return Files.readAllBytes(Paths.get((String) data));
                } else {
                    throw new IllegalArgumentException("Data must be a File to read your request.");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        final String contentType;
        final BiFunction<Object, Gson, byte[]> format;

        BodyType(String contentType, BiFunction<Object, Gson, byte[]> format) {
            this.contentType = contentType;
            this.format = format;
        }

        protected RequestBody formatContent(@Nullable Object data, Gson gson) {
            if (data != null) {
                return RequestBody.create(MediaType.parse(contentType), format.apply(data, gson));
            } else return null;
        }
    }
}
