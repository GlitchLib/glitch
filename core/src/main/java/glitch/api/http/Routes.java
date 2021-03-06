package glitch.api.http;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class Routes {
    private final HttpMethod method;
    private final String endpoint;

    private Routes(HttpMethod method, String endpoint) {
        this.method = method;
        this.endpoint = endpoint;
    }

    public static Routes create(HttpMethod method, String endpoint) {
        return new Routes(method, endpoint);
    }

    public static Routes get(String endpoint) {
        return create(HttpMethod.GET, endpoint);
    }

    public static Routes post(String endpoint) {
        return create(HttpMethod.POST, endpoint);
    }

    public static Routes put(String endpoint) {
        return create(HttpMethod.PUT, endpoint);
    }

    public static Routes patch(String endpoint) {
        return create(HttpMethod.PATCH, endpoint);
    }

    public static Routes delete(String endpoint) {
        return create(HttpMethod.DELETE, endpoint);
    }

    public static Routes options(String endpoint) {
        return create(HttpMethod.OPTIONS, endpoint);
    }

    public HttpRequest newRequest(Object... parameters) {
        return new HttpRequest(method, formatEndpoint(endpoint, parameters));
    }

    private String formatEndpoint(String endpoint, Object... parameters) {
        if (parameters.length > 0) {
            return String.format(endpoint, parameters);
        } else return endpoint;
    }
}
