package glitch.auth.store;

import glitch.auth.objects.json.Credential;
import java.util.function.Predicate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Empty Storage where they are not saving or storing anywhere.
 */
public class EmptyStorage implements Storage {
    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Credential> register(Credential credential) {
        return Mono.just(credential);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> drop(Credential credential) {
        return Mono.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Credential> fetchAll() {
        return Flux.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Credential> get(Predicate<Credential> condition) {
        return Flux.empty();
    }
}
