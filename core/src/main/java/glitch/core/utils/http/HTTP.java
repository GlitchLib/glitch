package glitch.core.utils.http;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import static feign.Request.Options;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HTTP {
    public static Feign create(Multimap<String, String> headers, @Nonnull Gson gson) {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder(gson))
                .encoder(new GsonEncoder(gson))
                .errorDecoder(new GlitchErrorDecoder(gson))
                .options(new Options())
                .requestInterceptor(new HeaderInterceptor(headers))
                .contract(new JAXRSContract())
                .logger(new Slf4jLogger("glitch.client.http"))
                .logLevel(Logger.Level.FULL)
                .build();
    }

    @RequiredArgsConstructor
    private static class HeaderInterceptor implements RequestInterceptor {
        private final Multimap<String, String> headers;

        @Override
        public void apply(RequestTemplate template) {
            if (headers != null && !headers.isEmpty()) {
                template.headers(headers.asMap());
            }
        }
    }
}
