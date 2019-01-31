package glitch.api.http;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import glitch.api.objects.adapters.ColorAdapter;
import glitch.api.objects.adapters.SubscriptionTypeAdapter;
import glitch.api.objects.adapters.UserTypeAdapter;
import glitch.api.objects.enums.SubscriptionType;
import glitch.api.objects.enums.UserType;
import glitch.auth.GlitchScope;
import glitch.auth.objects.adapters.ScopeAdapter;
import glitch.exceptions.http.ResponseException;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * The HTTP Client provided for {@link glitch.service.AbstractHttpService}
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class HttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    private final String baseUrl;

    private final OkHttpClient httpClient;
    private final Gson gson;

    /**
     * Glitch HTTP Client
     * @param httpClient HTTP Client
     * @param gson JSON IConverter
     * @param baseUrl Base URL
     */
    private HttpClient(OkHttpClient httpClient, Gson gson, String baseUrl) {
        if (!baseUrl.matches("^http(s)?://(.+)")) {
            throw new IllegalArgumentException("Base URL must contain a \"http\" prefix.");
        }
        this.httpClient = httpClient;
        this.gson = gson;
        this.baseUrl = baseUrl;
    }

    public Gson getGson() {
        return gson;
    }

    /**
     * Creating HTTP Request
     * @param method HTTP Method
     * @param endpoint API Endpoint
     * @return HTTP Request which handled into {@link #exchange(HttpRequest)} method
     * @see #exchange(HttpRequest)
     * @deprecated use {@link glitch.api.http.Routes#create(HttpMethod, String)}
     */
    @Deprecated
    public final HttpRequest create(HttpMethod method, String endpoint) {
        return new HttpRequest(method, endpoint);
    }

    /**
     * Starting exchange data of Http Client
     * @param request the {@link HttpRequest {@code HttpRequest}} filled to requesting subject
     * @return Non throwing response if any subjects are be fulfilled. If it throws exceptions will be return {@code null}
     */
    public final Mono<HttpResponse> exchange(HttpRequest request) {
        return Mono.create(sink -> {
            try {
                sink.success(doResponse(request, httpClient.newCall(doRequest(request)).execute()));
            } catch (IOException e) {
                sink.error(e);
            }
        });
    }

    /**
     * Starting exchange data of Http Client
     * @param request the {@link HttpRequest {@code HttpRequest}} filled to requesting subject
     * @param type format to the Response Type
     * @return Non throwing response if any subjects are be fulfilled. If it throws exceptions will be return {@code null}
     */
    @SuppressWarnings("unchecked")
    public final <T> Mono<T> exchangeAs(HttpRequest request, Class<T> type) {
        return exchange(request).flatMap(response -> Mono.create(sink -> {
            if (type == HttpResponse.class) {
                sink.success((T) response);
            } else if (response.isSuccessful()) {
                try {
                    sink.success(response.getBodyAs(type));
                } catch (JsonSyntaxException ex) {
                    sink.error(ex);
                }
            } else {
                try {
                    sink.error(Objects.requireNonNull(response.getBodyAs(ResponseException.class)));
                } catch (JsonSyntaxException | NullPointerException ex) {
                    sink.error(new ResponseException(response.getStatus()));
                }
            }
        }));
    }

    private Request doRequest(HttpRequest request) {
        Request.Builder builder = new Request.Builder()
                .method(request.method.name(), (request.bodyType != null) ? request.bodyType.formatContent(request.body, gson) : null)
                .url(doFormatUrl(request.endpoint, request.queryParams));

        if (!request.headers.isEmpty()) {
            request.headers.entries().forEach(e -> builder.header(e.getKey(), e.getValue()));
        }

        return builder.build();
    }

    private String doFormatUrl(String endpoint, MultiValuedMap<String, Object> queryParams) {
        if (endpoint.matches("^http(s)://(.+)")) {
            return endpoint + queryParams.entries().stream()
                    .map(e -> e.getKey() + "=" + e.getValue().toString() )
                    .collect(Collectors.joining("&", "?", ""));
        } else if (endpoint.startsWith("/")) {
            return baseUrl + endpoint + queryParams.entries().stream()
                    .map(e -> e.getKey() + "=" + e.getValue().toString() )
                    .collect(Collectors.joining("&", "?", ""));
        } else {
            throw new IllegalArgumentException("Endpoint is not matched format");
        }
    }

    private HttpResponse doResponse(HttpRequest request, Response response) throws IOException {
        byte[] body = (response.body() != null) ? response.body().bytes() : null;
        MultiValuedMap<String, String> headers = MultiMapUtils.newListValuedHashMap();

        response.headers().toMultimap().forEach(headers::putAll);
        return new HttpResponse(
                    new HttpResponse.Status(response.code(), response.message()),
                    MultiMapUtils.unmodifiableMultiValuedMap(headers),
                    body, request, gson);

    }

    /**
     * Build HTTP Client
     * @return the Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final MultiValuedMap<String, String> headers = new HashSetValuedHashMap<>();
        private final GsonBuilder gsonBuilder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        private String baseUrl;

        /**
         * Add Header
         * @param key header key
         * @param value header Value
         * @return the Builder
         */
        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        /**
         * Add Header
         * @param headers {@link Map} of Headers
         * @return the Builder
         */
        public Builder addHeaders(Map<String, String> headers) {
            headers.forEach(this::addHeader);
            return this;
        }

        /**
         * Add Type Adapter
         * @param type {@link Class} type
         * @param adapter Adapter
         * @return the Builder
         */
        public Builder addTypeAdapter(Type type, Object adapter) {
            this.gsonBuilder.registerTypeAdapter(type, adapter);
            return this;
        }

        /**
         * Add Type Adapter
         * @param adapters {@link Map} of Adapters
         * @return the Builder
         */
        public Builder addTypeAdapters(Map<Type, Object> adapters) {
            adapters.forEach(this::addTypeAdapter);
            return this;
        }

        /**
         * Adds Base URL
         * @param baseUrl Base URL
         * @return the Builder
         */
        public Builder withBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * Injecting Default Adapters and providing it into other API's
         * @return the Builder
         */
        public Builder withDefaultTypeAdapters() {
            addTypeAdapter(GlitchScope.class, new ScopeAdapter());
            addTypeAdapter(Color.class, new ColorAdapter());
            addTypeAdapter(UserType.class, new UserTypeAdapter());
            addTypeAdapter(SubscriptionType.class, new SubscriptionTypeAdapter());
            return this;
        }

        /**
         * Initialize HTTP Client
         * @return HTTP Client
         */
        public HttpClient build() {
            Objects.requireNonNull(baseUrl, "base_url == null");

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            if (!headers.isEmpty()) {
                httpClient.addInterceptor(chain -> {
                    Request.Builder builder = chain.request().newBuilder();

                    headers.entries().forEach(e -> builder.addHeader(e.getKey(), e.getValue()));

                    return chain.proceed(builder.build());
                });
            }

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(LOG::debug).setLevel(HttpLoggingInterceptor.Level.HEADERS);

            interceptor.redactHeader("Client-ID");
            interceptor.redactHeader("Authorization");

            httpClient.addInterceptor(interceptor);

            return new HttpClient(httpClient.build(), gsonBuilder.create(), baseUrl);
        }
    }
}
