package glitch.core.utils.api;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClient {
    private final OkHttpClient httpClient;
    private final ObjectMapper mapper;

    // TODO: Use logger to debugging stuff
    private final Logger logger = LoggerFactory.getLogger("glitch.http.client");

    public static Builder builder() {
        return new Builder();
    }

    Single<String> buildBody(Object body) {
        return Single.create(s -> s.onSuccess(mapper.writeValueAsString(body)));
    }

    @SuppressWarnings("unchecked")
    <R> Single<R> exchange(Request request, Class<R> responseType) {
        return Single.create(s -> {
            try (Response response = httpClient.newCall(request).execute()) {
                if (responseType == Response.class) {
                    s.onSuccess((R) response);
                } else if (responseType == ResponseBody.class && response.body() != null) {
                    s.onSuccess((R) response.body());
                } else {
                    if (!response.isSuccessful()) {
                        ResponseError error = (response.body() != null) ?
                                mapper.readValue(response.body().bytes(), ResponseError.class) :
                                new ResponseError(null, 0, null);

                        if (error.getMessage() == null || error.getMessage().equals("")) {
                            error.setMessage(response.message());
                        }

                        if (error.getStatus() == 0) {
                            error.setStatus(response.code());
                        }

                        if (error.getError() == null || error.getError().equals("")) {
                            error.setError(response.message());
                        }

                        throw new ResponseException(error);
                    } else {
                        if (response.body() != null && response.body().contentLength() > 0 && responseType != Void.class)
                            s.onSuccess(mapper.readValue(response.body().bytes(), responseType));
                    }
                }
            } catch (IOException io) {
                s.onError(io);
            }
        });
    }

    public static class Builder {
        private final Map<String, String> headers = new LinkedHashMap<>();
        private final SimpleModule module = new SimpleModule();
        private final ObjectMapper mapper = new ObjectMapper();

        private Builder() {
        }

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
                    .addInterceptor(chain -> {
                        Request.Builder request = chain.request().newBuilder();

                        if (!headers.isEmpty()) {
                            request.headers(Headers.of(headers));
                        }

                        return chain.proceed(request.build());
                    }).build();


            return new HttpClient(httpClient, mapper.registerModule(module));
        }
    }
}
