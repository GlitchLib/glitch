package glitch.auth.json;

import glitch.auth.Scope;
import glitch.core.utils.Immutable;
import java.util.Set;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface Validate {
    String getClientId();

    String getLogin();

    Set<Scope> getScopes();

    Long getUserId();
}
