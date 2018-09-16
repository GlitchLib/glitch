package glitch.auth.store;

import com.google.common.collect.Collections2;
import glitch.auth.Credential;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Predicate;
import java.util.Collection;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CachedStorage implements Storage {
    private final Collection<Credential> credentials;

    @Override
    public Single<Void> register(final Credential credential) {
        return Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> sub) throws Exception {
                drop(credential);
                credentials.add(credential);
            }
        });
    }

    @Override
    public Single<Void> drop(final Credential credential) {
        return Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> sub) throws Exception {
                Collection<Credential> filter = Collections2.filter(credentials, new com.google.common.base.Predicate<Credential>() {
                    @Override
                    public boolean apply(@Nullable Credential input) {
                        return input != null && input.getUserId().equals(credential.getUserId());
                    }
                });

                for (Credential cr : filter) {
                    credentials.remove(cr);
                }
            }
        });
    }

    @Override
    public Observable<Credential> fetchAll() {
        return Observable.fromIterable(credentials);
    }

    @Override
    public Observable<Credential> get(Predicate<Credential> condition) {
        return fetchAll().filter(condition);
    }

    @Override
    public Single<Credential> getById(final Long id) {
        return get(new Predicate<Credential>() {
            @Override
            public boolean test(Credential credential) throws Exception {
                return credential.getUserId().equals(id);
            }
        }).firstOrError();
    }

    @Override
    public Maybe<Credential> getByLogin(final String loginRegex) {
        return get(new Predicate<Credential>() {
            @Override
            public boolean test(Credential credential) throws Exception {
                return credential.getLogin().matches(loginRegex);
            }
        }).firstElement();
    }
}
