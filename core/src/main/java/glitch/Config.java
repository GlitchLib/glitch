package glitch;

import glitch.auth.GlitchScope;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class Config {
    private final String clientId;
    private final String clientSecret;
    private final String userAgent;
    private final Set<GlitchScope> defaultScopes;
    @Nullable private final String redirectUri;

    public Config(String clientId, String clientSecret, String userAgent, Set<GlitchScope> defaultScopes, @Nullable String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userAgent = userAgent;
        this.defaultScopes = defaultScopes;
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Set<GlitchScope> getDefaultScopes() {
        return defaultScopes;
    }

    @Nullable
    public String getRedirectUri() {
        return redirectUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Config)) return false;
        Config config = (Config) o;
        return getClientId().equals(config.getClientId()) &&
                getClientSecret().equals(config.getClientSecret()) &&
                getUserAgent().equals(config.getUserAgent()) &&
                getDefaultScopes().equals(config.getDefaultScopes()) &&
                Objects.equals(getRedirectUri(), config.getRedirectUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientId(), getClientSecret(), getUserAgent(), getDefaultScopes(), getRedirectUri());
    }

    @Override
    public String toString() {
        return "Config{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", defaultScopes=" + defaultScopes +
                ", redirectUri='" + redirectUri + '\'' +
                '}';
    }
}
