package glitch.auth.store;

import glitch.auth.objects.json.Credential;
import java.util.Collection;
import java.util.function.Predicate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Cached storage storing only in active JVM session using {@link Collection}
 */
public class CachedStorage implements Storage {
    private final Collection<Credential> credentials;

    public CachedStorage(Collection<Credential> credentials) {
        this.credentials = credentials;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Credential> register(Credential credential) {
        credentials.stream().filter(c -> c.getUserId().equals(credential.getUserId()))
                .findFirst().ifPresent(this::drop);
        credentials.add(credential);

        return Mono.just(credential);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> drop(Credential credential) {
        return Mono.just(credentials.removeIf(c -> c.getUserId().equals(credential.getUserId())))
                .then();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Credential> fetchAll() {
        return Flux.fromIterable(credentials);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Credential> get(Predicate<Credential> condition) {
        return fetchAll().filter(condition);
    }
}
