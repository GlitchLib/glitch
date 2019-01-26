package glitch.auth.objects.json.impl;

import com.google.gson.annotations.SerializedName;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.AccessToken;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;

public final class AccessTokenImpl implements AccessToken {
    private final String accessToken;

    private final String refreshToken;

    @SerializedName("expires_in")
    private final Instant expiredAt;

    @SerializedName("scope")
    private final Set<GlitchScope> scopes;

    public AccessTokenImpl(String accessToken, String refreshToken, Instant expiredAt, Set<GlitchScope> scopes) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
        this.scopes = scopes;
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
    @Override
    public Instant getExpiredAt() {
        return expiredAt;
    }

    @Override
    public Set<GlitchScope> getScopes() {
        return scopes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessTokenImpl)) return false;
        AccessTokenImpl that = (AccessTokenImpl) o;
        return getAccessToken().equals(that.getAccessToken()) &&
                getRefreshToken().equals(that.getRefreshToken()) &&
                getExpiredAt().equals(that.getExpiredAt()) &&
                getScopes().equals(that.getScopes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccessToken(), getRefreshToken(), getExpiredAt(), getScopes());
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiredAt=" + expiredAt +
                ", scopes=" + scopes +
                '}';
    }
}
