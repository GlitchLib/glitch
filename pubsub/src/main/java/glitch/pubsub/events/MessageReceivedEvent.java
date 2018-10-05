package glitch.pubsub.events;

import glitch.pubsub.topics.Topic;
import glitch.socket.utils.EventImmutable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface MessageReceivedEvent extends Message {

    DataReceive getData();

    public interface DataReceive {
        Topic getTopic();

        String getMessage();
    }
}
