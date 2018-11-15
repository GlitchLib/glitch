package glitch.auth.store;

import glitch.auth.Credential;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

public class EmptyStorage implements Storage {
    @Override
    public Mono<Credential> register(Credential credential) {
        return Mono.just(credential);
    }

    @Override
    public Mono<Void> drop(Credential credential) {
        return Mono.empty();
    }

    @Override
    public Flux<Credential> fetchAll() {
        return Flux.empty();
    }

    @Override
    public Flux<Credential> get(Predicate<Credential> condition) {
        return Flux.empty();
    }
}
