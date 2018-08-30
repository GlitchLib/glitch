package glitch.api;

import java.util.concurrent.atomic.AtomicReference;

public class BaseURL {
    private final String baseUrl;
    private final AtomicReference<String> endpoint = new AtomicReference<>();

    private BaseURL(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public BaseURL endpoint(String endpoint) {
        this.endpoint.set(endpoint);
        return this;
    }

    String generate(Object... values) {
        StringBuilder sb = new StringBuilder(baseUrl);

        if (!baseUrl.endsWith("/") || !endpoint.get().startsWith("/")) {
            sb.append("/");
        }
        sb.append(String.format(endpoint.get(), values));

        return sb.toString();
    }

    public static BaseURL create(String baseUrl) {
        return new BaseURL(baseUrl);
    }
}
