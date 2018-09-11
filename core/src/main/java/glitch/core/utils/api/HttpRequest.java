package glitch.core.utils.api;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.reactivex.Single;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest<R> {
    private final String uri;
    private final HttpMethod method;
    private final Class<R> responseType;
    private final Multimap<String, Object> queryParameters = MultimapBuilder
            .linkedHashKeys()
            .linkedHashSetValues()
            .build();
    private final Multimap<String, String> headers = MultimapBuilder
            .linkedHashKeys()
            .linkedHashSetValues()
            .build();
    private final AtomicReference<Object> body = new AtomicReference<>();

    HttpRequest(HttpMethod method, String uri, Class<R> responseType) {
        this.method = method;
        this.uri = uri;
        this.responseType = responseType;
    }

    public HttpRequest<R> header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpRequest<R> queryParam(String key, Object value) {
        queryParameters.put(key, value);
        return this;
    }

    public Single<R> exchange(HttpClient httpClient) {
        StringBuilder url = new StringBuilder(uri);

        if (!queryParameters.isEmpty()) {
            url.append(queryParameters.entries()
                    .stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&", "?", "")));
        }
        Single<RequestBody> requestBody = (body.get() != null) ?
                httpClient.buildBody(body.get()).map(body -> RequestBody.create(MediaType.parse("application/json"), body)) :
                Single.never();

        return requestBody.map(body -> new Request.Builder()
                .method(method.name(), body).url(url.toString()).build())
                .flatMap(request -> httpClient.exchange(request, responseType));
    }
}
