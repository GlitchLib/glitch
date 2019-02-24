package glitch.pubsub;

import glitch.api.http.Unofficial;
import glitch.auth.objects.json.Credential;
import java.nio.charset.Charset;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Topic {
    private final Topic.Type type;
    private final String[] suffix;
    @Nullable
    private final Credential credential;

    public Topic(Type type, String[] suffix, Credential credential) {
        this.type = type;
        this.suffix = suffix;
        this.credential = credential;
    }

    public UUID getCode() {
        return UUID.nameUUIDFromBytes(getRawType().getBytes(Charset.forName("UTF-8")));
    }

    public String getRawType() {
        return getType().toRaw(suffix);
    }

    public String toString() {
        return String.format("Topic(\"%s\")", getRawType());
    }

    @Nonnull
    public Type getType() {
        return this.type;
    }

    public String[] getSuffix() {
        return this.suffix;
    }

    @Nullable
    public Credential getCredential() {
        return this.credential;
    }

    enum Type {
        /**
         * Anyone cheers on a specified channel.
         */
        CHANNEL_BITS("channel-bits-events-v1"),

        /**
         * Anyone cheers on a specified channel.
         */
        CHANNEL_BITS_V2("channel-bits-events-v2"),

        /**
         * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
         * <p>
         * Subgift subscription messages contain recipient information.
         * Formatting:
         */
        CHANNEL_SUBSCRIPTION("channel-subscribe-events-v1"),

        /**
         * Anyone whispers the specified user.
         */
        WHISPERS("whispers"),

        /**
         * Anyone follow on a specified channel.
         * Formatting: {@code {&quot;display_name&quot;:&quot;&lt;display name&gt;&quot;, &quot;username&quot;:&quot;&lt;username&gt;&quot;, &quot;user_id&quot;:&quot;&lt;id&gt;&quot;}}
         */
        @Unofficial("[Unknown Source]") // TODO: Required Source
                FOLLOW("following"),

        /**
         * Listening moderation actions in specific channel.
         * Owner ID must be a moderator in specific channel.
         */
        @Unofficial("https://discuss.dev.twitch.tv/t/in-line-broadcaster-chat-mod-logs/7281")
        CHAT_MODERATION_ACTIONS("chat_moderator_actions"),

        /**
         * Listens EBS broadcast sent to specific extension on a specific channel
         */
        @Unofficial("https://discuss.dev.twitch.tv/t/private-topic-for-extension-events-in-pubsub/15628/3")
        CHANNEL_EXTENSION_BROADCAST("channel-ext-v1") {
            @Override
            public String toRaw(String... subject) {
                return super.toRaw(subject) + "-broadcast";
            }
        },

        /**
         * Anyone makes a purchase on a channel.
         */
        CHANNEL_COMMERCE("channel-commerce-events-v1"),

        /**
         * Listening live stream with view counter in specific channel name
         */
        @Unofficial("https://discuss.dev.twitch.tv/t/pubsub-video-playback/9020")
        VIDEO_PLAYBACK("video-playback");


        final String value;

        Type(String value) {
            this.value = value;
        }

        static Type readType(String raw) {
            for (Type t : values()) {
                if (t.value.equals(raw)) return t;
            }
            throw new IllegalArgumentException("Cannot handle unknown raw type: " + raw);
        }

        String toRaw(String... subject) {
            return String.format("%s.%s", value, String.join(".", subject));
        }

        public String getValue() {
            return this.value;
        }
    }
}
