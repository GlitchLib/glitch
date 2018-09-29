package glitch.pubsub.events.topics;

import glitch.core.api.json.IDObject;
import glitch.socket.utils.EventImmutable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface Emote extends IDObject<Long> {
    int getStart();
    int getEnd();
}
