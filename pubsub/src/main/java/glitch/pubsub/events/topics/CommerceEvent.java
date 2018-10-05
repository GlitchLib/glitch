package glitch.pubsub.events.topics;

import glitch.pubsub.GlitchPubSub;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import java.time.Instant;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface CommerceEvent extends Event<GlitchPubSub> {
    String getUserName();

    String getDisplayName();

    String getChannelName();

    Long getChannelId();

    Long getUserId();

    Instant getTime();

    String getItemImageUrl();

    String getItemDescription();

    boolean isSupportsChannel();

    Message getPurchaseMessage();
}
