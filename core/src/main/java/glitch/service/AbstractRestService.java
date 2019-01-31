package glitch.service;

import glitch.GlitchClient;
import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.auth.GlitchScope;
import glitch.exceptions.ServiceNotFoundException;
import glitch.exceptions.http.ResponseException;
import glitch.exceptions.http.ScopeIsMissingException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import reactor.core.publisher.Mono;

/**
 * The Abstract REST Service for Kraken and Helix endpoints
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class AbstractRestService implements IService {
    private final GlitchClient client;
    private final HttpClient httpClient;
    private final Set<AbstractHttpService> services = new LinkedHashSet<>();

    protected AbstractRestService(GlitchClient client, HttpClient httpClient) {
        this.client = client;
        this.httpClient = httpClient;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public GlitchClient getClient() {
        return client;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    protected final <T extends AbstractHttpService> boolean register(T service) {
        return this.services.add(service);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractHttpService> CompletableFuture<T> use(Class<T> service) {
        CompletableFuture<T> future = new CompletableFuture<>();

        try {
            future.complete((T) this.services.stream().filter(s -> s.getClass().isAssignableFrom(service)).findFirst().orElseThrow(() ->new ServiceNotFoundException(service)));
        } catch (ServiceNotFoundException e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    protected final <T extends AbstractHttpService> boolean unregister(T service) {
        return this.services.remove(service);
    }

    protected final void unregisterAll() {
        this.services.clear();
    }

}
