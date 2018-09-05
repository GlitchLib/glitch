package glitch.auth.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.auth.Scope;
import glitch.common.utils.Immutable;
import java.util.Set;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = ValidateBuilder.class)
public interface Validate {
    String getClientId();
    String getLogin();
    Set<Scope> getScopes();
    Long getUserId();
}
