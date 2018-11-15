package glitch;

import glitch.auth.Scope;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Config {
    private final String clientId;
    private final String clientSecret;
    private final String userAgent;
    private final Set<Scope> defaultScopes;
    @Nullable private final String redirectUri;
}
