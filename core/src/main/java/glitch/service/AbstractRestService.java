package glitch.service;

import glitch.GlitchClient;
import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.auth.GlitchScope;
import glitch.exceptions.ServiceNotFoundException;
import glitch.exceptions.http.ResponseException;
import glitch.exceptions.http.ScopeIsMissingException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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

    /**
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    public static abstract class AbstractRequest<R> {

        protected final HttpClient httpClient;
        protected final HttpRequest<R> request;

        /**
         * Creates instance of Abstract Request
         * @param httpClient HTTP Client
         * @param request Request
         */
        protected AbstractRequest(HttpClient httpClient, HttpRequest<R> request) {
            this.httpClient = httpClient;
            this.request = request;
        }

        public R block() throws ResponseException, IOException {
            return httpClient.exchange(request).getBody();
        }

        public void async(Consumer<R> response, Consumer<Throwable> exceptions) {
            try {
                response.accept(block());
            } catch (Exception e) {
                exceptions.accept(e);
            }
        }

        public void async(Consumer<R> response) {
            async(response, ex -> {});
        }

        /**
         * Checks Required scopes
         *
         * @param scopes        list of {@link glitch.auth.GlitchScope}
         * @param requiredScope required {@link glitch.auth.GlitchScope}
         * @return scope is exist
         */
        protected final boolean checkRequiredScope(Collection<GlitchScope> scopes, GlitchScope requiredScope) {
            return scopes.contains(requiredScope);
        }

        /**
         * Handling {@link glitch.exceptions.http.ScopeIsMissingException}
         *
         * @param requiredScope required {@link glitch.auth.GlitchScope}
         * @return throwable exception called {@link glitch.exceptions.http.ScopeIsMissingException}
         */
        protected ScopeIsMissingException handleScopeMissing(GlitchScope requiredScope) {
            return new ScopeIsMissingException(requiredScope);
        }
    }
}
