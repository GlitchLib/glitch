package glitch.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.utils.Immutable;
import io.reactivex.Single;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = CredentialBuilder.class)
public interface Credential extends AccessToken, Validate {

    final class Initializer {
        final AtomicReference<String> accessToken = new AtomicReference<>();
        final AtomicReference<String> refreshToken = new AtomicReference<>();

        public Initializer accessToken(String accessToken) {
            this.accessToken.set(accessToken);
            return this;
        }
        public Initializer refreshToken(String refreshToken) {
            this.refreshToken.set(refreshToken);
            return this;
        }

        Single<Credential> create(CredentialManager manager) {
            Objects.requireNonNull(accessToken.get());
            Objects.requireNonNull(refreshToken.get());

            return manager.rebuild(new CredentialBuilder().accessToken(accessToken.get()).refreshToken(refreshToken.get()).build());
        }
    }
}
