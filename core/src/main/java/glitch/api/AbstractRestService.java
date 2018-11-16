package glitch.api;

import glitch.GlitchClient;
import glitch.api.http.GlitchHttpClient;
import glitch.exceptions.ServiceNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.Set;

@RequiredArgsConstructor
public abstract class AbstractRestService<T extends AbstractHttpService> {
    @Getter
    private final GlitchClient client;
    @Getter
    private final GlitchHttpClient httpClient;
    private final Set<T> services = new LinkedHashSet<>();

    protected final boolean register(T service) {
        return this.services.add(service);
    }

    @SuppressWarnings("unchecked")
    public Mono<T> use(Class<T> service) {
        return Flux.fromIterable(this.services)
                .filter(s -> s.getClass().equals(service)).next().cast(service)
                .switchIfEmpty(Mono.error(new ServiceNotFoundException(service)));
    }

    protected final boolean unregister(T service) {
        return this.services.remove(service);
    }

    protected final void unregisterAll() {
        this.services.clear();
    }
}
