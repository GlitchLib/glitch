package glitch.pubsub.object.json;

import com.google.common.collect.ImmutableSet;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.enums.UserType;
import glitch.api.objects.json.Badge;
import glitch.api.objects.json.interfaces.IDObject;
import glitch.pubsub.object.json.message.Emote;
import lombok.Data;

import java.awt.*;
import java.time.Instant;

@Data
public class WhisperMessage implements IDObject<Long> {
    private final Long id;

    @SerializedName("body")
    private final String message;

    @SerializedName("sent_ts")
    private final Instant createdAt;

    @SerializedName("from_id")
    private final Long senderId;

    private final Tags tags;

    private final Recipient recipient;

    @Data
    public class Tags {
        private final String login;
        private final String displayName;
        private final Color color;
        private final UserType userType;
        private final ImmutableSet<Emote> emotes;
        private final ImmutableSet<Badge> badges;
    }

    @Data
    public class Recipient implements IDObject<Long> {
        private final Long id;
        private final String username;
        private final String displayName;
        private final Color color;
        private final UserType userType;
        private final ImmutableSet<Badge> badges;
        private final String profileImage;
    }
}
