package glitch.core.utils.api;

public class Router<R> {
    private final BaseURL url;
    private final HttpMethod method;
    private final Class<R> responseType;

    public Router(HttpMethod method, BaseURL url, Class<R> responseType) {
        this.method = method;
        this.url = url;
        this.responseType = responseType;
    }

    public static <R> Router<R> create(HttpMethod method, BaseURL url, Class<R> responseType) {
        if (!url.hasEndpoint()) throw new NullPointerException("Endpoint is missing!");
        return new Router<>(method, url, responseType);
    }

    public HttpRequest<R> request(Object... parameters) {
        return new HttpRequest<>(method, url.generate(parameters), responseType);
    }
}
