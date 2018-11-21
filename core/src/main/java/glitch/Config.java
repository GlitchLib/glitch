package glitch;

import glitch.auth.Scope;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Set;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Config {
    private final String clientId;
    private final String clientSecret;
    private final String userAgent;
    private final Set<Scope> defaultScopes;
    @Nullable private final String redirectUri;
}
