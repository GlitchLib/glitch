package glitch.pubsub;

import glitch.api.http.Unofficial;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Topic {
    private final Topic.Type type;
    private final String[] suffix;
    @Nullable
    private final Credential credential;

    public UUID getCode() {
        return UUID.nameUUIDFromBytes(getRawType().getBytes(Charset.forName("UTF-8")));
    }

    public String getRawType() {
        return getType().toRaw(suffix);
    }

    public String toString() {
        return String.format("Topic(\"%s\")", getRawType());
    }

    /**
     * Anyone cheers on a specified channel.
     */
    public static Topic bits(Long channelId, Credential credential) {
        return new Topic(Type.CHANNEL_BITS, toArray(channelId), credential);
    }

    /**
     * Anyone cheers on a authorized channel.
     */
    public static Topic bits(Credential credential) {
        return bits(credential.getUserId(), credential);
    }

    /**
     * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
     * <p>
     * Subgift subscription messages contain recipient information.
     */
    public static Topic subscription(Long channelId, Credential credential) throws ScopeIsMissingException {
        if (credential.getScopes().contains(GlitchScope.CHANNEL_SUBSCRIPTIONS)) {
            return new Topic(Type.CHANNEL_SUBSCRIPTION, toArray(channelId), credential);
        } else throw new ScopeIsMissingException(GlitchScope.CHANNEL_SUBSCRIPTIONS);
    }

    /**
     * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a authorized channel.
     * <p>
     * Subgift subscription messages contain recipient information.
     */
    public static Topic subscription(Credential credential) throws ScopeIsMissingException {
        return subscription(credential.getUserId(), credential);
    }

    /**
     * Anyone whispers the specified user.
     */
    public static Topic whispers(Credential credential) throws ScopeIsMissingException {
        if (credential.getScopes().contains(GlitchScope.CHAT_LOGIN) || credential.getScopes().contains(GlitchScope.WHISPERS_READ)) {
            return new Topic(Type.WHISPERS, toArray(credential.getUserId()), credential);
        } else throw new ScopeIsMissingException(GlitchScope.WHISPERS_READ);
    }

    /**
     * Anyone follow on a specified channel.
     */
    public static Topic following(Long channelId) {
        return new Topic(Type.FOLLOW, toArray(channelId), null);
    }

    /**
     * Listening moderation actions in specific channel.
     * Owner ID must be a moderator in specific channel.
     */
    public static Topic moderationActions(Long channelId, Credential credential) throws ScopeIsMissingException {
        if (credential.getScopes().contains(GlitchScope.CHAT_LOGIN) || credential.getScopes().contains(GlitchScope.CHANNEL_MODERATE)) {
            return new Topic(Type.CHAT_MODERATION_ACTIONS, toArray(channelId, credential.getUserId()), credential);
        } else throw new ScopeIsMissingException(GlitchScope.CHANNEL_MODERATE);
    }

    /**
     * Listening moderation actions on the own channel.
     */
    public static Topic moderationActions(Credential credential) throws ScopeIsMissingException {
        return moderationActions(credential.getUserId(), credential);
    }

    /**
     * Listens EBS broadcast sent to specific extension on a specific channel
     */
    public static Topic extensionBroadcast() {
        throw new UnsupportedOperationException("Extensions is currently unsupported");
    }

    /**
     * Listening live stream with view counter in specific channel name
     */
    public static Topic videoPlayback(String channelName) {
        return new Topic(Type.VIDEO_PLAYBACK, toArray(channelName), null);
    }

    /**
     * Anyone makes a purchase on a channel.
     */
    public static Topic commerce(Long channelId, Credential credential) {
        return new Topic(Type.CHANNEL_COMMERCE, toArray(channelId), credential);
    }

    /**
     * Anyone makes a purchase on your channel.
     */
    public static Topic commerce(Credential credential) {
        return commerce(credential.getUserId(), credential);
    }

    private static <S extends Serializable> String[] toArray(S... serialized) {
        return Arrays.stream(serialized).map(String::valueOf).toArray(String[]::new);
    }

    public static Topic fromRaw(String raw) {
        String[] split = raw.split(".");

        String[] suffix = Arrays.stream(split).filter(e -> !e.equals(split[0]))
                .map(s -> s.replace("-broadcast", ""))
                .toArray(String[]::new);

        if (split.length > 2 && split[0].equals(Type.CHAT_MODERATION_ACTIONS.value)) {
            return new Topic(Type.CHAT_MODERATION_ACTIONS, suffix, null);
        } else {
            return new Topic(Type.readType(split[0]), suffix, null);
        }
    }

    @RequiredArgsConstructor
    enum Type {
        /**
         * Anyone cheers on a specified channel.
         */
        CHANNEL_BITS("channel-bits-events-v1"),

        /**
         * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
         * <p>
         * Subgift subscription messages contain recipient information.
         * Formatting:
         *
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


        @Getter
        private final String value;

        private static Type readType(String raw) {
            for (Type t : values()) {
                if (t.value.equals(raw)) return t;
            }
            return null;
        }

        String toRaw(String... subject) {
            return String.format("%s.%s", value, String.join(".", subject));
        }
    }
}
