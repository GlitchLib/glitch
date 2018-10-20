package glitch.pubsub.events;

import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface TopicRegisteredEvent extends Event<GlitchPubSub> {
    Topic getTopic();
}
