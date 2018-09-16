package glitch.auth.store;

import glitch.auth.Credential;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Predicate;

public class EmptyStorage implements Storage {
    @Override
    public Single<Void> register(Credential credential) {
        return Single.never();
    }

    @Override
    public Single<Void> drop(Credential credential) {
        return Single.never();
    }

    @Override
    public Observable<Credential> fetchAll() {
        return Observable.empty();
    }

    @Override
    public Observable<Credential> get(Predicate<Credential> condition) {
        return Observable.empty();
    }

    @Override
    public Single<Credential> getById(Long id) {
        return Single.never();
    }

    @Override
    public Maybe<Credential> getByLogin(String loginRegex) {
        return Maybe.empty();
    }
}
