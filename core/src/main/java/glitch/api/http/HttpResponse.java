package glitch.api.http;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * HTTP Response {@link GlitchHttpClient#exchange(HttpRequest) Exchanged} via {@link HttpRequest}
 * Response Body can be composed using {@link #toMono()} or {@link #toFlux(Function)}.
 * The last one must be composed into implemented {@link Iterable} interface.
 * @param <R> Response Type
 */
@Data
public class HttpResponse<R> {
    private final Status status;
    private final ImmutableListMultimap<String, String> headers;
    @Getter(AccessLevel.NONE)
    private final Callable<R> body;
    private final HttpRequest<R> request;

    /**
     * Call to non-blocking {@link Mono} Response
     * @return a {@link Mono} Response
     */
    public Mono<R> toMono() {
        return Mono.fromCallable(body);
    }

    /**
     * Call to non-blocking {@link Mono} Response with mapping your own body
     * @return a {@link Mono} Response
     */
    public <T> Mono<T> toMono(Function<R, T> bodyMapper) {
        return toMono().map(bodyMapper);
    }


    /**
     * Call to non-blocking {@link Flux} Response with mapping {@link Iterable} interface body
     * @return a {@link Flux} Response
     */
    public <T> Flux<T> toFlux(Function<R, Iterable<T>> bodyMapper) {
        return toMono().flatMapIterable(bodyMapper);
    }

    /**
     * Consume Response Body
     * @param body Response Body
     * @param exception Exception if exist
     */
    public void getBody(Consumer<R> body, Consumer<Throwable> exception) {
        toMono().subscribe(body, exception);
    }

    /**
     * Getting Body with throwing exception
     * @throws java.io.IOException Response can't be converted into propertly type
     */
    public R getBodyBlocking() throws Exception {
        return body.call();
    }

    /**
     * Getting values of Key Header
     * @param key Header Key
     * @return {@link java.util.List} of values
     */
    public ImmutableList<String> getHeader(String key) {
        return getHeaders().get(key);
    }

    /**
     * Getting value of Key Header and value index
     * @param key Header Key
     * @param index Header Value index
     * @return Header Value
     */
    public String getHeader(String key, int index) {
        return getHeader(key).get(index);
    }

    /**
     * Status codes are in the HTTP Error Code range
     * @return error status
     */
    public boolean isError() {
        return status.code >= 400 && status.code < 600;
    }

    /**
     * Status codes are in the HTTP Code range
     * @return successful executed response
     */
    public boolean isSuccessful() {
        return status.code >= 200 && status.code < 400;
    }

    /**
     * Response Status POJO
     */
    @Data
    public static class Status {
        private final int code;
        private final String message;
    }
}
