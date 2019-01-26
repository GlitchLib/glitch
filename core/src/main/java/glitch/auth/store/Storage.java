package glitch.auth.store;

import glitch.auth.objects.json.Credential;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

/**
 * This API storing your credentials into your own custom data storage.
 * You can use it to build own storage for storing credential data.
 */
public interface Storage {

    /**
     * Register created credential. Useful after creating authorization.
     * You can replace your credentials using {@link Credential#getUserId()}
     * @param credential User Credential
     * @return Non-blocking response data creation with returning data.
     */
    Mono<Credential> register(Credential credential);

    /**
     * Removing credentials of your storage
     * @param credential User Credential
     * @return A removed or not removed (if not exist) will forwarding to success executing this method.
     */
    Mono<Void> drop(Credential credential);

    /**
     * Getting all credentials
     * @return {@link Iterable} credentials
     */
    Flux<Credential> fetchAll();

    /**
     * Getting all credentials with filters
     * @param condition your filter
     * @return filtered {@link Iterable} credentials
     */
    Flux<Credential> get(Predicate<Credential> condition);

    /**
     * Getting credential by ID
     * @param id User ID
     * @return finded credential
     */
    default Mono<Credential> getById(Long id) {
        return get(credential -> credential.getUserId().equals(id)).next();
    }

    /**
     * Getting credentials by Login filtered via <a href="https://www.javatpoint.com/java-regex">Regular Expression</a>
     * @param loginRegex RegEx-ed login by example: {@code ^(l|i)(.+)$} - will be find a credentials starts {@code l} or {@code i}
     * @return multiple or one queries of credentials
     */
    default Flux<Credential> getByLogin(String loginRegex) {
        return get(credential -> credential.getLogin().matches(loginRegex));
    }
}
