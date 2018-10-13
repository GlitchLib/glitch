package glitch.pubsub.events.topics;

import glitch.pubsub.GlitchPubSub;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable(copy = true)
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface FollowEvent extends Event<GlitchPubSub> {
    default Long getChannelId() { return 0L; }

    String getDisplayName();

    String getUsername();

    Long getUserId();
}
