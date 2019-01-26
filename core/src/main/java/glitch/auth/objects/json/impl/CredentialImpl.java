package glitch.auth.objects.json.impl;

import com.google.gson.annotations.SerializedName;
import glitch.auth.GlitchScope;
import glitch.auth.UserCredential;
import glitch.auth.objects.json.AccessToken;
import glitch.auth.objects.json.Credential;
import glitch.auth.objects.json.Validate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import reactor.util.function.Tuple2;

public final class CredentialImpl implements Credential {

    private final String accessToken;

    private final String refreshToken;

    @Nullable
    @SerializedName("expires_in")
    private final Instant expiredAt;

    @SerializedName(value = "scope", alternate = "scopes")
    private final Set<GlitchScope> scopes;

    private final String clientId;

    private final String login;

    private final Long userId;

    public CredentialImpl(String accessToken, String refreshToken, @Nullable Instant expiredAt, Set<GlitchScope> scopes, String clientId, String login, Long userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
        this.scopes = scopes;
        this.clientId = clientId;
        this.login = login;
        this.userId = userId;
    }

    public CredentialImpl(Tuple2<AccessToken, Validate> tuple) {
        this(tuple.getT1(), tuple.getT2());
    }

    public CredentialImpl(AccessToken accessToken, Validate validate) {
        this(accessToken.getAccessToken(), accessToken.getRefreshToken(), accessToken.getExpiredAt(), validate.getScopes(), validate.getClientId(), validate.getLogin(), validate.getUserId());
    }

    public CredentialImpl(Validate validate, UserCredential userCredential) {
        this(userCredential.getAccessToken(), userCredential.getRefreshToken(), Instant.now().plus(60, ChronoUnit.DAYS), validate.getScopes(), validate.getClientId(), validate.getLogin(), validate.getUserId());
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Nullable
    public Instant getExpiredAt() {
        return expiredAt;
    }

    @Override
    public Set<GlitchScope> getScopes() {
        return scopes;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CredentialImpl)) return false;
        CredentialImpl that = (CredentialImpl) o;
        return getAccessToken().equals(that.getAccessToken()) &&
                getRefreshToken().equals(that.getRefreshToken()) &&
                getExpiredAt().equals(that.getExpiredAt()) &&
                getScopes().equals(that.getScopes()) &&
                getClientId().equals(that.getClientId()) &&
                getLogin().equals(that.getLogin()) &&
                getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccessToken(), getRefreshToken(), getExpiredAt(), getScopes(), getClientId(), getLogin(), getUserId());
    }

    @Override
    public String toString() {
        return "Credential{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiredAt=" + expiredAt +
                ", scopes=" + scopes +
                ", clientId='" + clientId + '\'' +
                ", login='" + login + '\'' +
                ", userId=" + userId +
                '}';
    }
}
