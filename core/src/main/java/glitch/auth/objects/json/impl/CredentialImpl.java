package glitch.auth.objects.json.impl;

import com.google.gson.annotations.SerializedName;
import glitch.auth.Scope;
import glitch.auth.UserCredential;
import glitch.auth.objects.json.AccessToken;
import glitch.auth.objects.json.Credential;
import glitch.auth.objects.json.Validate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import reactor.util.function.Tuple2;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Data
@RequiredArgsConstructor
public final class CredentialImpl implements Credential {

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

    public CredentialImpl(Tuple2<AccessToken, Validate> tuple) {
        this(tuple.getT1(), tuple.getT2());
    }

    public CredentialImpl(AccessToken accessToken, Validate validate) {
        this(accessToken.getAccessToken(), accessToken.getRefreshToken(), accessToken.expiredAt(), validate.getScopes(), validate.getClientId(), validate.getLogin(), validate.getUserId());
    }

    public CredentialImpl(Validate validate, UserCredential userCredential) {
        this(userCredential.getAccessToken(), userCredential.getRefreshToken(), Instant.now().plus(60, ChronoUnit.DAYS), validate.getScopes(), validate.getClientId(), validate.getLogin(), validate.getUserId());
    }
}
