package glitch.auth.store;

import glitch.auth.Credential;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

public interface Storage {
    Mono<Credential> register(Credential credential);

    Mono<Void> drop(Credential credential);

    Flux<Credential> fetchAll();

    Flux<Credential> get(Predicate<Credential> condition);

    default Mono<Credential> getById(Long id) {
        return get(credential -> credential.getUserId().equals(id)).next();
    }

    default Mono<Credential> getByLogin(String loginRegex) {
        return get(credential -> credential.getLogin().matches(loginRegex)).next();
    }
}
