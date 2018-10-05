package glitch.pubsub.json.request;

import glitch.core.utils.Immutable;
import glitch.pubsub.json.Data;
import java.util.Set;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface Request extends Data {
    Set<String> getTopics();

    String authToken();
}
