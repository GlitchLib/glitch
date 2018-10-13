package glitch.pubsub.events.topics;

import com.google.common.collect.ImmutableSet;
import com.google.gson.annotations.SerializedName;
import glitch.core.api.json.Badge;
import glitch.core.api.json.IDObject;
import glitch.core.api.json.enums.UserType;
import glitch.pubsub.GlitchPubSub;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import java.awt.Color;
import java.time.Instant;
import javax.annotation.Nullable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface WhisperEvent extends Event<GlitchPubSub>, IDObject<Integer> {
    @SerializedName("body")
    String getMessage();

    @SerializedName("sent_ts")
    Instant getCreatedAt();

    @SerializedName("from_id")
    Long getSenderId();

    Tags getTags();

    Recipient getRecipient();

    @EventImmutable
    @Value.Immutable
    @Gson.TypeAdapters(fieldNamingStrategy = true)
    interface Tags {
        String getLogin();

        String getDisplayName();

        Color getColor();

        UserType getUserType();

        ImmutableSet<Emote> getEmotes();

        ImmutableSet<Badge> getBadges();
    }

    @EventImmutable
    @Value.Immutable
    @Gson.TypeAdapters(fieldNamingStrategy = true)
    interface Recipient extends IDObject<Long> {
        String getUsername();

        String getDisplayName();

        Color getColor();

        UserType getUserType();

        ImmutableSet<Badge> getBadges();

        @Nullable
        String getProfileImage();
    }
}
