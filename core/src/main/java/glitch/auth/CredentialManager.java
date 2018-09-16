package glitch.auth;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import glitch.GlitchClient;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.auth.json.converters.ExpireInstantDeserializer;
import glitch.auth.store.EmptyStorage;
import glitch.auth.store.Storage;
import glitch.core.api.AbstractService;
import glitch.core.utils.GlitchUtils;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

public final class CredentialManager extends AbstractService<CredentialAPI> {
    private static final String BASE_URL = "https://id.twitch.tv/oauth2";

    private final Storage storage;

    public CredentialManager(GlitchClient client, @Nullable Storage storage) {
        super(client, GlitchUtils.createHttpClient(BASE_URL, null, serializers(), deserializers()).create(CredentialAPI.class));
        this.storage = (storage != null) ? storage : new EmptyStorage();
    }

    public Single<Validate> validate(Credential credential) {
        return api.validate(credential);
    }

    public Single<Credential> create(String code, String redirectUri) {
        return api.getAccessToken(
                client.getConfiguration().getClientId(),
                client.getConfiguration().getClientSecret(),
                code,
                Objects.requireNonNull((redirectUri != null) ? redirectUri : client.getConfiguration().getRedirectUri(), "redirect_uri")
        ).flatMap(new Function<AccessToken, SingleSource<? extends Credential>>() {
            @Override
            public SingleSource<? extends Credential> apply(AccessToken accessToken) throws Exception {
                return CredentialManager.this.build(accessToken);
            }
        });
    }

    public Single<Credential> refresh(Credential credential) {
        return api.refreshToken(
                client.getConfiguration().getClientId(),
                client.getConfiguration().getClientSecret(),
                credential
        ).flatMap(new Function<AccessToken, SingleSource<Credential>>() {
            @Override
            public SingleSource<Credential> apply(AccessToken accessToken) throws Exception {
                return CredentialManager.this.build(accessToken);
            }
        });
    }

    private Single<Credential> build(AccessToken accessToken) {
        final CredentialImpl.Builder cb = CredentialImpl.builder()
                .from(accessToken);

        return validate(cb.build())
                .map(new Function<Validate, Credential>() {
                    @Override
                    public Credential apply(Validate validate) throws Exception {
                        return cb.from(validate).build();
                    }
                })
                .doOnSuccess(new Consumer<Credential>() {
                    @Override
                    public void accept(Credential credential) throws Exception {
                        storage.register(credential);
                    }
                });
    }

    public Single<Void> revoke(final Credential credential) {
        return api.revoke(credential.getClientId(), credential)
                .doOnSuccess(new Consumer<Void>() {
                    @Override
                    public void accept(Void v) throws Exception {
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

        return validate(cb.build())
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

    public AuthorizationUriBuilder buildAuthorization() {
        return new AuthorizationUriBuilder(BASE_URL, client.getConfiguration());
    }

    @SuppressWarnings("unchecked")
    private static <X> Map<Class<X>, JsonSerializer<X>> serializers() {
        Map<Class<X>, JsonSerializer<X>> serializers = new LinkedHashMap<>();

        GlitchUtils.registerSerializers(serializers);
        serializers.put((Class<X>) Calendar.class, (JsonSerializer<X>) new ExpireInstantDeserializer());

        return serializers;
    }

    @SuppressWarnings("unchecked")
    private static <X> Map<Class<X>, JsonDeserializer<X>> deserializers() {
        Map<Class<X>, JsonDeserializer<X>> deserializers = new LinkedHashMap<>();

        GlitchUtils.registerDeserializers(deserializers);
        deserializers.put((Class<X>) Calendar.class, (JsonDeserializer<X>) new ExpireInstantDeserializer());

        return deserializers;
    }
}
