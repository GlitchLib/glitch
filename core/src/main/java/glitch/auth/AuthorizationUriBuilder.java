package glitch.auth;

import glitch.Config;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class AuthorizationUriBuilder {
    private final String baseUrl;
    private final Config config;
    private final Set<GlitchScope> scopes = new LinkedHashSet<>();
    private String state;
    private String redirectUri;
    private AtomicBoolean forceVerify = new AtomicBoolean(false);

    AuthorizationUriBuilder(String baseUrl, Config config) {
        this.baseUrl = baseUrl;
        this.config = config;
        this.redirectUri = config.getRedirectUri();
    }

    public AuthorizationUriBuilder addScope(GlitchScope... scopes) {
        return addScope(Arrays.asList(scopes));
    }

    public AuthorizationUriBuilder addScope(Collection<GlitchScope> scopes) {
        this.scopes.addAll(scopes);
        return this;
    }

    public AuthorizationUriBuilder withState(String state) {
        this.state = state;
        return this;
    }

    public AuthorizationUriBuilder withForceVerify() {
        this.forceVerify.set(true);
        return this;
    }

    public AuthorizationUriBuilder withoutForceVerify() {
        this.forceVerify.set(false);
        return this;
    }

    public AuthorizationUriBuilder withRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder(baseUrl)
                .append("/authorize?response_type=code")
                .append("&client_id=").append(config.getClientId())
                .append("&redirect_uri=").append(Objects.requireNonNull((redirectUri != null && !redirectUri.equals("")) ? redirectUri : config.getRedirectUri(), "redirect_uri == null"))
                .append("&scope=").append(buildScope());

        if (forceVerify.get()) {
            sb.append("&force_verify=true");
        }

        if (state != null && !state.equals("")) {
            sb.append("&state=").append(state);
        }

        return sb.toString();
    }

    private String buildScope() {
        return (scopes.isEmpty()) ? "" : scopes.stream().map(GlitchScope::getValue).collect(Collectors.joining("+"));
    }
}
