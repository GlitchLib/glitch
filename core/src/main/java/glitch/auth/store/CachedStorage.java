package glitch.auth.store;

import glitch.auth.Credential;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CachedStorage implements Storage {
    private final Collection<Credential> credentials;

    @Override
    public Mono<Credential> register(Credential credential) {
        credentials.stream().filter(c -> c.getUserId().equals(credential.getUserId()))
                .findFirst().ifPresent(this::drop);
        credentials.add(credential);

        return Mono.just(credential);
    }

    @Override
    public Mono<Void> drop(Credential credential) {
        return Mono.just(credentials.removeIf(c -> c.getUserId().equals(credential.getUserId())))
                .then();
    }

    @Override
    public Flux<Credential> fetchAll() {
        return Flux.fromIterable(credentials);
    }

    @Override
    public Flux<Credential> get(Predicate<Credential> condition) {
        return fetchAll().filter(condition);
    }
}
