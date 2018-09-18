package glitch.auth;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import glitch.Config;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class AuthorizationUriBuilder {
    private final String baseUrl;
    private final Config config;
    private final Set<Scope> scopes = new LinkedHashSet<>();
    private String state;
    private String redirectUri;
    private boolean forceVerify = false;

    AuthorizationUriBuilder(String baseUrl, Config config) {
        this.baseUrl = baseUrl;
        this.config = config;
        this.redirectUri = config.getRedirectUri();
    }

    public AuthorizationUriBuilder addScope(Scope... scopes) {
        return addScope(Arrays.asList(scopes));
    }

    public AuthorizationUriBuilder addScope(Collection<Scope> scopes) {
        this.scopes.addAll(scopes);
        return this;
    }

    public String create() {
        StringBuilder sb = new StringBuilder(baseUrl)
                .append("/authorize?response_type=code")
                .append("&client_id=").append(config.getClientId())
                .append("&redirect_uri=").append(Objects.requireNonNull((redirectUri != null && !redirectUri.equals("")) ? redirectUri : config.getRedirectUri(), "redirect_uri"))
                .append("&scope=").append(buildScope());

        if (forceVerify) {
            sb.append("&force_verify=").append(true);
        }

        if (state != null && !state.equals("")) {
            sb.append("&state=").append(state);
        }

        return sb.toString();
    }

    private String buildScope() {
        return Joiner.on('+')
                .join(Collections2.transform(
                        (!scopes.isEmpty()) ?
                                scopes :
                                config.getDefaultScopes(),
                        new com.google.common.base.Function<Scope, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Scope input) {
                if (input != null) {
                    return input.getValue();
                } else return null;
            }
        }));

    }
}
