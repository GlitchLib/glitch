package glitch.pubsub.json;

import glitch.core.utils.Immutable;
import glitch.pubsub.events.Message;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface DataMessage<T extends Data> extends Message {
    T getData();
}
