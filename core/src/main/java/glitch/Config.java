package glitch;

import glitch.auth.Scope;
import glitch.core.utils.Immutable;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Config {
    String getClientId();

    String getClientSecret();

    String getUserAgent();

    Set<Scope> getDefaultScopes();

    @Nullable
    String getRedirectUri();
}
