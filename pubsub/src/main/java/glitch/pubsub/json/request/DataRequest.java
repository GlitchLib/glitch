package glitch.pubsub.json.request;

import glitch.core.utils.Immutable;
import glitch.pubsub.json.DataMessage;
import java.util.UUID;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface DataRequest extends DataMessage<Request> {
    default String getNonce() {
        return UUID.randomUUID().toString();
    }
}
