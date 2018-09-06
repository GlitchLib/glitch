package glitch.auth.store;

import glitch.auth.Credential;
import io.reactivex.Single;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public interface Storage {
    void registerAsync(Credential credential) throws IOException;

    default Single<Void> register(Credential credential) {
        return Single.fromCallable(() -> {
            registerAsync(credential);
            return null;
        });
    }

    void dropAsync(Credential credential) throws IOException;

    default Single<Void> drop(Credential credential) {
        return Single.fromCallable(() -> {
            dropAsync(credential);
            return null;
        });
    }

    Collection<Credential> fetchAll();

    Optional<Credential> getAsync(Predicate<Credential> condition) throws IOException;

    Credential getByIdAsync(Long id) throws IOException;

    Optional<Credential> getByLoginAsync(String loginRegex) throws IOException;

    default Single<Credential> get(Predicate<Credential> condition) {
        return Single.create(sub -> getAsync(condition).ifPresent(sub::onSuccess));
    }

    default Single<Credential> getById(Long id) {
        return get(c -> c.getUserId().equals(id));
    }

    default Single<Credential> getByName(String loginRegex) {
        return get(c -> c.getLogin().matches(loginRegex));
    }
}
