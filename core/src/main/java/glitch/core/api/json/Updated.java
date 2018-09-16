package glitch.core.api.json;

import glitch.core.utils.Immutable;
import java.time.Instant;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface Updated {
    Instant updatedAt();
}
