package glitch.pubsub.events.topics;

import glitch.socket.utils.EventImmutable;
import java.util.List;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
interface Message {
    String getMessage();

    List<Emote> getEmotes();
}