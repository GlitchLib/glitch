package glitch.auth;

import com.google.gson.annotations.SerializedName;
import glitch.auth.objects.AccessToken;
import glitch.auth.objects.Validate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Set;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class CredentialImpl implements Credential{

    private final String accessToken;

    private final String refreshToken;

    @Nullable
    @Accessors(fluent = true)
    @SerializedName("expires_in")
    private final Instant expiredAt;

    @SerializedName(value = "scope", alternate = "scopes")
    private final Set<Scope> scopes;

    private final String clientId;

    private final String login;

    private final Long userId;

    CredentialImpl(AccessToken accessToken, Validate validate) {
        this(accessToken.getAccessToken(), accessToken.getRefreshToken(), accessToken.expiredAt(), validate.getScopes(), validate.getClientId(), validate.getLogin(), validate.getUserId());
    }

    CredentialImpl(UserCredential userCredential, Validate validate) {
        this(userCredential.getAccessToken(), userCredential.getRefreshToken(), null, validate.getScopes(), validate.getClientId(), validate.getLogin(), validate.getUserId());
    }
}
