package glitch.core.utils.api;

import java.util.concurrent.atomic.AtomicReference;

public class BaseURL {
    private final String base;
    private final AtomicReference<String> endpoint = new AtomicReference<>();

    private BaseURL(String baseUrl) {
        this.base = baseUrl;
    }

    public static BaseURL create(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return new BaseURL(baseUrl);
    }

    public BaseURL endpoint(String endpoint) {
        this.endpoint.set(endpoint);
        return this;
    }

    boolean hasEndpoint() {
        return endpoint.get() != null && endpoint.get().matches("^/(.+)$");
    }

    String generate(Object... values) {
        return base + String.format(endpoint.get(), values);
    }
}
