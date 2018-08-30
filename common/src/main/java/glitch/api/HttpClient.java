package glitch.api;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {
    private final OkHttpClient httpClient;
    private final ObjectMapper mapper;

    private final Logger logger = LoggerFactory.getLogger("glitch.http.client");

    private HttpClient(OkHttpClient httpClient, ObjectMapper mapper) {
        this.httpClient = httpClient;
        this.mapper = mapper;
    }

    String buildBody(Object body) throws IOException {
        return mapper.writeValueAsString(body);
    }

    @SuppressWarnings("unchecked")
    <R> Single<R> exchange(Request request, Class<R> responseType) {
        return (Single<R>) Single.create(source -> {
            Response response = httpClient.newCall(request).execute();

            if (!response.isSuccessful()) {
                ResponseError error = mapper.readValue(response.body().bytes(), ResponseError.class);

                if (error.getMessage() == null || error.getMessage().equals("")) {
                    error.setMessage(response.message());
                }

                if (error.getStatus() == 0) {
                    error.setStatus(response.code());
                }

                if (error.getError() == null || error.getError().equals("")) {
                    error.setError(response.message());
                }

                source.onError(new ResponseException(error));
            }

            if (responseType == Response.class) {
                source.onSuccess(response);
            } else {
                if (response.body() != null && response.body().contentLength() > 0)
                    source.onSuccess(mapper.readValue(response.body().bytes(), responseType));
                else source.onSuccess(null);
            }
        }).doOnError(e -> logger.error(e.getMessage(), e));
    }

    <R> R exchangeAsync(Request request, Class<R> responseType) {
        return exchange(request, responseType).blockingGet();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, String> headers = new LinkedHashMap<>();
        private final SimpleModule module = new SimpleModule();
        private final ObjectMapper mapper = new ObjectMapper();

        private Builder() {}

        public Builder header(String key, String value) {
            headers.put(key, value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> Builder deserializer(JsonDeserializer<T> deserializer) {
            Class<T> typeClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return deserializer(typeClass, deserializer);
        }

        public <T> Builder deserializer(Class<T> typeClass, JsonDeserializer<T> deserializer) {
            module.addDeserializer(typeClass, deserializer);
            return this;
        }

        public Builder buildMapper(Consumer<ObjectMapper> mapper) {
            try {
                mapper.accept(this.mapper);
            } catch (Exception ignore) {

            }
            return this;
        }

        public HttpClient build() {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder request = chain.request().newBuilder();

                            if (!headers.isEmpty()) {
                                request.headers(Headers.of(headers));
                            }

                            return chain.proceed(request.build());
                        }
                    }).build();


            return new HttpClient(httpClient, mapper.registerModule(module));
        }
    }
}
