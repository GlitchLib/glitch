package glitch.core.utils.http;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import java.io.IOException;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import static feign.Request.Options;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HTTP {
    public static Feign.Builder create(Multimap<String, String> headers, @Nonnull final Gson gson) {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder(gson) {
                    @Override
                    public Object decode(final Response response, Type type) throws IOException {
                        if (type == Completable.class) {
                            return Completable.create(new CompletableOnSubscribe() {
                                @Override
                                public void subscribe(CompletableEmitter emitter) throws Exception {
                                    if (response.status() >= 400) {
                                        emitter.onError(new GlitchErrorDecoder(gson).decode(null, response));
                                    } else  {
                                        emitter.onComplete();
                                    }
                                }
                            });
                        } else return super.decode(response, type);
                    }
                })
                .encoder(new GsonEncoder(gson))
                .errorDecoder(new GlitchErrorDecoder(gson))
                .options(new Options())
                .requestInterceptor(new HeaderInterceptor(headers))
                .contract(new JAXRSContract())
                .logger(new Slf4jLogger("glitch.client.http"))
                .logLevel(Logger.Level.FULL);
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
