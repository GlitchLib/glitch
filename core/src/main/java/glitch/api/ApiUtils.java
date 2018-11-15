package glitch.api;

import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * API Utilities provides a static methods for Services
 */
class ApiUtils {
    /**
     * Creating HTTP Client
     * @param loggingLevel Logging Level
     * @param headers Default Headers injected for each requests
     * @return HTTP Client
     */
    public static OkHttpClient getHttpClient(HttpLoggingInterceptor.Level loggingLevel, Multimap<String, String> headers) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(LoggerFactory.getLogger("glitch.api")::info)
                        .setLevel(loggingLevel));

        if (headers != null && !headers.isEmpty()) {
            builder.addInterceptor(new HeaderInterceptor(headers));
        }

        return builder.build();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class HeaderInterceptor implements Interceptor {
        private final Multimap<String, String> headers;

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();

            headers.forEach(builder::addHeader);

            return chain.proceed(builder.build());
        }
    }
}
