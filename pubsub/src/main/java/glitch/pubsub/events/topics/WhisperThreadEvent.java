package glitch.pubsub.events.topics;

import com.google.gson.annotations.SerializedName;
import glitch.pubsub.GlitchPubSub;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import java.time.Instant;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface WhisperThreadEvent extends Event<GlitchPubSub> {
    @SerializedName("archived")
    boolean isArchived();
    @SerializedName("muted")
    boolean isMuted();
    Instant whitelistedUntil();
}
