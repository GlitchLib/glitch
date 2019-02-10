package glitch.service;

import glitch.GlitchClient;
import glitch.api.http.HttpClient;
import glitch.exceptions.ServiceNotFoundException;
import java.util.LinkedHashSet;
import java.util.Set;
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
    public <T extends AbstractHttpService> Mono<T> use(Class<T> service) {
        return Mono.create(sink -> {
            for (AbstractHttpService s : this.services) {
                if (s.getClass() == service) {
                    sink.success((T) s);
                    return;
                }
            }
            sink.error(new ServiceNotFoundException(service));
        });
    }

    protected final <T extends AbstractHttpService> boolean unregister(T service) {
        return this.services.remove(service);
    }

    protected final void unregisterAll() {
        this.services.clear();
    }

}
