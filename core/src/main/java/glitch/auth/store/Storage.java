package glitch.auth.store;

import glitch.auth.Credential;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Predicate;

public interface Storage {
    Single<Void> register(Credential credential);

    Single<Void> drop(Credential credential);

    Observable<Credential> fetchAll();

    Observable<Credential> get(Predicate<Credential> condition);

    Single<Credential> getById(Long id);

    Maybe<Credential> getByLogin(String loginRegex);
}
