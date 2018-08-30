package glitch.api;

import com.google.common.base.Joiner;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest<R> {
    private final String uri;
    private final HttpMethod method;
    private final Class<R> responseType;

    HttpRequest(HttpMethod method, String uri, Class<R> responseType) {
        this.method = method;
        this.uri = uri;
        this.responseType = responseType;
    }

    private final Multimap<String, ? extends Serializable> queryParameters = MultimapBuilder
            .linkedHashKeys()
            .linkedHashSetValues()
            .build();

    private final Multimap<String, String> headers = MultimapBuilder
            .linkedHashKeys()
            .linkedHashSetValues()
            .build();

    private final AtomicReference<Object> body = new AtomicReference<>();

    public R exchangeAsync(HttpClient httpClient) {
        return exchange(httpClient).blockingGet();
    }

    public Single<R> exchange(final HttpClient httpClient) {
        StringBuilder url = new StringBuilder(uri);

        if (!queryParameters.isEmpty()) {
            url.append(queryParameters.entries()
                    .stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&", "?", "")));
        }
        
        try {
            Request.Builder request = new Request.Builder()
                    .method(method.name(),
                            (body.get() == null) ?
                                    null :
                                    RequestBody.create(
                                            MediaType.parse("application/json"),
                                            httpClient.buildBody(body.get()))
                    ).url(url.toString());
            return httpClient.exchange(request.build(), responseType);
        } catch (IOException e) {
            return Single.error(e);
        }
    }
}
