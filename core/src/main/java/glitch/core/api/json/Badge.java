package glitch.core.api.json;

import glitch.core.utils.Immutable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface Badge {
    int getVersion();

    String getName();
}
