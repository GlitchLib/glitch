package glitch.api.http;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import glitch.api.objects.adapters.ColorAdapter;
import glitch.api.objects.adapters.SubscriptionTypeAdapter;
import glitch.api.objects.adapters.UserTypeAdapter;
import glitch.api.objects.enums.SubscriptionType;
import glitch.api.objects.enums.UserType;
import glitch.auth.Scope;
import glitch.auth.objects.adapters.ScopeAdapter;
import glitch.exceptions.http.ResponseException;
import lombok.Getter;
import okhttp3.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The HTTP Client provided for {@link glitch.api.AbstractHttpService}
 */
public class GlitchHttpClient {
    private final String baseUrl;

    private final OkHttpClient httpClient;
    @Getter
    private final Gson gson;

    @Nullable
    private final ExceptionHandler exceptionHandler;

    /**
     * Glitch HTTP Client
     * @param httpClient HTTP Client
     * @param gson JSON Converter
     * @param baseUrl Base URL
     * @param exceptionHandler Exception handler
     */
    private GlitchHttpClient(OkHttpClient httpClient, Gson gson, String baseUrl, @Nullable ExceptionHandler exceptionHandler) {
        if (!baseUrl.matches("^http(s)://(.+)")) {
            throw new IllegalArgumentException("Base URL must contain a HTTP prefix.");
        }
        this.httpClient = httpClient;
        this.gson = gson;
        this.baseUrl = baseUrl;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Creating HTTP Request
     * @param method HTTP Method
     * @param endpoint API Endpoint
     * @param responseType {@link Class} Response Type
     * @param <T> Response Type
     * @return HTTP Request which handled into {@link #exchange(HttpRequest)} method
     * @see #exchange(HttpRequest)
     */
    public final <T> HttpRequest<T> create(HttpMethod method, String endpoint, Class<T> responseType) {
        return new HttpRequest<>(method, endpoint, responseType);
    }

    /**
     * Starting exchange data from Http Client
     * @param httpRequest the {@link HttpRequest {@code HttpRequest}} filled to requesting subject
     * @param <R> Response Type Class
     * @return Non throwing response if any subjects are be fulfilled. If it throws exceptions will be return {@code null}
     * @throws RuntimeException will throwing {@link RuntimeException {@code RuntimeException}} if {@link ExceptionHandler {@code ExceptionHandler}} is not defined to handling error messages
     */
    public final <R> HttpResponse<R> exchange(HttpRequest<R> httpRequest) {
        Request request = doRequest(httpRequest);

        try (Response response = httpClient.newCall(request).execute()) {
            ImmutableListMultimap.Builder<String, String> headersBuilder = ImmutableListMultimap.builder();
            response.headers().toMultimap().forEach(headersBuilder::putAll);
            Callable<R> responseBody = () -> {
                ResponseBody body = response.body();
                if (body != null) {
                    return gson.fromJson(response.body().charStream(), httpRequest.responseType);
                } else {
                    return null;
                }
            };

            if (!response.isSuccessful()) {
                throw gson.fromJson(response.body().charStream(), ResponseException.class);
            } else {
                return new HttpResponse<>(new HttpResponse.Status(response.code(), response.message()), headersBuilder.build(), responseBody, httpRequest);
            }

        } catch (IOException | ResponseException e) {
            if (exceptionHandler != null) {
                exceptionHandler.handle(e);
                return null;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Synchronized Exchange
     * @param request the {@link HttpRequest {@code HttpRequest}} filled to requesting subject
     * @param response Consuming {@link HttpResponse} if any exist
     * @param exceptions Consuming any exceptions if any exist
     * @param <R> Response Type Class
     */
    public final <R> void exchangeSync(HttpRequest<R> request,
                                 Consumer<HttpResponse<R>> response,
                                 Consumer<Throwable> exceptions) {
        try {
            response.accept(exchange(request));
        } catch (Exception e) {
            exceptions.accept(e);
        }
    }

    /**
     * Build HTTP Client
     * @return the Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    private Request doRequest(HttpRequest<?> httpRequest) {
        return new Request.Builder()
                .method(httpRequest.method.name(), getBody(httpRequest.body))
                .url(buildUrl(httpRequest.endpoint, httpRequest.queryParams))
                .headers(buildHeaders(httpRequest.headers)).build();
    }

    private Headers buildHeaders(Multimap<String, String> headers) {
        Headers.Builder builder = new Headers.Builder();
        headers.forEach(builder::add);
        return builder.build();
    }

    private String buildUrl(String endpoint, Multimap<String, ? extends Serializable> queryParams) {
        StringBuilder uri = new StringBuilder();

        if (endpoint.matches("^http(s)?://")) {
            uri.append(endpoint);
        } else {
            uri.append(baseUrl).append(endpoint);
        }

        if (!queryParams.isEmpty()) {
            uri.append(queryParams.entries().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&", "?", "")));
        }

        return uri.toString();
    }

    @Nullable
    private RequestBody getBody(@Nullable Object body) {
        if (body == null) return null;
        else return RequestBody.create(MediaType.get("application/json"), gson.toJson(body));
    }

    public static class Builder {
        private final Multimap<String, String> headers = LinkedListMultimap.create();
        private final GsonBuilder gsonBuilder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        private String baseUrl;

        @Nullable
        private ExceptionHandler exceptionHandler;

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
         * Adds Custom {@link ExceptionHandler}
         * @param exceptionHandler Exception Handler
         * @return the Builder
         * @see ExceptionHandler
         */
        public Builder withExceptionHandler(ExceptionHandler exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        /**
         * Injecting Default Adapters and providing it into other API's
         * @return
         */
        public Builder withDefaultTypeAdapters() {
            addTypeAdapter(Scope.class, new ScopeAdapter());
            addTypeAdapter(Color.class, new ColorAdapter());
            addTypeAdapter(UserType.class, new UserTypeAdapter());
            addTypeAdapter(SubscriptionType.class, new SubscriptionTypeAdapter());
            return this;
        }

        /**
         * Initialize HTTP Client
         * @return HTTP Client
         */
        public GlitchHttpClient build() {
            Objects.requireNonNull(baseUrl, "base_url == null");

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            if (!headers.isEmpty()) {
                httpClient.addInterceptor(chain -> {
                    Request.Builder builder = chain.request().newBuilder();

                    headers.forEach(builder::addHeader);

                    return chain.proceed(builder.build());
                });
            }

            return new GlitchHttpClient(httpClient.build(), gsonBuilder.create(), baseUrl, exceptionHandler);
        }
    }
}
