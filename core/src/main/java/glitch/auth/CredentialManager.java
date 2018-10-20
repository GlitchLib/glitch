package glitch.auth;

import com.google.common.collect.LinkedHashMultimap;
import glitch.GlitchClient;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.auth.json.converters.ExpireInstantDeserializer;
import glitch.auth.store.EmptyStorage;
import glitch.auth.store.Storage;
import glitch.core.api.AbstractService;
import glitch.core.utils.GlitchUtils;
import glitch.core.utils.http.HTTP;
import glitch.core.utils.http.ResponseException;
import io.reactivex.*;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

public final class CredentialManager extends AbstractService<CredentialAPI> {
    private final Storage storage;

    public CredentialManager(GlitchClient client, @Nullable Storage storage) {
        super(client, HTTP.create(
                LinkedHashMultimap.<String, String>create(),
                GlitchUtils.createGson(CredentialManager.adapters(), true))
                .target(new OAuth2Instance()));
        this.storage = (storage != null) ? storage : new EmptyStorage();
    }

    public Single<Validate> validate(final Credential credential) {
        return validate(credential.getAccessToken());
    }

    private Single<Validate> validate(final String accessToken) {
        return Single.unsafeCreate(new SingleSource<Validate>() {
            @Override
            public void subscribe(SingleObserver<? super Validate> observer) {
                try {
                    observer.onSuccess(api.validate("OAuth " + accessToken));
                } catch (ResponseException e) {
                    observer.onError(e);
                }
            }
        });
    }

    public Single<Credential> create(final String code, final String redirectUri) {
        return Single.unsafeCreate(new SingleSource<AccessToken>() {
            @Override
            public void subscribe(SingleObserver<? super AccessToken> observer) {
                try {
                    observer.onSuccess(api.getAccessToken(
                            client.getConfiguration().getClientId(),
                            client.getConfiguration().getClientSecret(),
                            code,
                            Objects.requireNonNull((redirectUri != null) ? redirectUri : client.getConfiguration().getRedirectUri(), "redirect_uri")
                    ));
                } catch (ResponseException e) {
                    observer.onError(e);
                }
            }
        }).flatMap(new Function<AccessToken, SingleSource<? extends Credential>>() {
            @Override
            public SingleSource<? extends Credential> apply(AccessToken accessToken) throws Exception {
                return CredentialManager.this.build(accessToken);
            }
        });
    }

    public Single<Credential> refresh(final Credential credential) {
        return Single.unsafeCreate(new SingleSource<AccessToken>() {
            @Override
            public void subscribe(SingleObserver<? super AccessToken> observer) {
                try {
                    observer.onSuccess(api.refreshToken(
                            client.getConfiguration().getClientId(),
                            client.getConfiguration().getClientSecret(),
                            credential.getRefreshToken()
                    ));
                } catch (ResponseException e) {
                    observer.onError(e);
                }
            }
        }).flatMap(new Function<AccessToken, SingleSource<Credential>>() {
            @Override
            public SingleSource<Credential> apply(AccessToken accessToken) throws Exception {
                return CredentialManager.this.build(accessToken);
            }
        });
    }

    private Single<Credential> build(AccessToken accessToken) {
        final CredentialImpl.Builder cb = CredentialImpl.builder()
                .from(accessToken);

        return validate(accessToken.getAccessToken())
                .map(new Function<Validate, Credential>() {
                    @Override
                    public Credential apply(Validate validate) throws Exception {
                        return cb.from(validate).build();
                    }
                })
                .cast(Credential.class)
                .doOnSuccess(new Consumer<Credential>() {
                    @Override
                    public void accept(Credential credential) throws Exception {
                        storage.register(credential);
                    }
                });
    }

    public Completable revoke(final Credential credential) {
        return Completable.unsafeCreate(new CompletableSource() {
            @Override
            public void subscribe(CompletableObserver cs) {
                try {
                    api.revoke(credential.getClientId(), credential.getAccessToken());
                    cs.onComplete();
                } catch (ResponseException e) {
                    cs.onError(e);
                }
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() {
                storage.drop(credential);
            }
        });
    }

    public Single<Credential> buildFromCredentials(String accessToken, String refreshToken) {
        return buildFromCredentials(new UserCredential(accessToken, refreshToken));
    }

    public Single<Credential> buildFromCredentials(UserCredential userCredential) {
        final CredentialImpl.Builder cb = CredentialImpl.builder()
                .accessToken(userCredential.getAccessToken())
                .refreshToken(userCredential.getRefreshToken());

        return validate(userCredential.getAccessToken())
                .map(new Function<Validate, Credential>() {
                    @Override
                    public Credential apply(Validate validate) throws Exception {
                        return cb.from(validate).build();
                    }
                }).doOnSuccess(new Consumer<Credential>() {
                    @Override
                    public void accept(Credential credential1) throws Exception {
                        storage.register(credential1);
                    }
                });
    }

    public Single<AuthorizationUriBuilder> buildAuthorization() {
        return Single.just(new AuthorizationUriBuilder(OAuth2Instance.BASE_URL, client.getConfiguration()));
    }

    @SuppressWarnings("unchecked")
    private static  Map<Type, Object> adapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        adapters.put(Calendar.class, new ExpireInstantDeserializer());

        return adapters;
    }
}
