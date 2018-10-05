package glitch.pubsub.topics;

import glitch.auth.Credential;
import glitch.auth.Scope;
import glitch.auth.ScopeIsMissingException;
import glitch.core.utils.Immutable;
import glitch.core.utils.Unofficial;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Topic {
    /**
     * Anyone cheers on a specified channel.
     */
    static Topic bits(Long channelId, Credential credential) {
        return TopicImpl.of(Type.CHANNEL_BITS, toArray(channelId), credential);
    }

    /**
     * Anyone cheers on a authorized channel.
     */
    static Topic bits(Credential credential) {
        return bits(credential.getUserId(), credential);
    }

    /**
     * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
     * <p>
     * Subgift subscription messages contain recipient information.
     */
    static Topic subscription(Long channelId, Credential credential) throws ScopeIsMissingException {
        if (credential.getScopes().contains(Scope.CHANNEL_SUBSCRIPTIONS)) {
            return TopicImpl.of(Type.CHANNEL_SUBSCRIPTION, toArray(channelId), credential);
        } else throw new ScopeIsMissingException(Scope.CHANNEL_SUBSCRIPTIONS);
    }

    /**
     * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a authorized channel.
     * <p>
     * Subgift subscription messages contain recipient information.
     */
    static Topic subscription(Credential credential) throws ScopeIsMissingException {
        return subscription(credential.getUserId(), credential);
    }

    /**
     * Anyone whispers the specified user.
     */
    static Topic whispers(Credential credential) throws ScopeIsMissingException {
        if (credential.getScopes().contains(Scope.CHAT_LOGIN)) {
            return TopicImpl.of(Type.WHISPERS, toArray(credential.getUserId()), credential);
        } else throw new ScopeIsMissingException(Scope.CHAT_LOGIN);
    }

    /**
     * Anyone follow on a specified channel.
     */
    static Topic following(Long channelId) {
        return TopicImpl.of(Type.FOLLOW, toArray(channelId), null);
    }

    /**
     * Listening moderation actions in specific channel.
     * Owner ID must be a moderator in specific channel.
     */
    static Topic moderationActions(Long channelId, Credential credential) {
        return TopicImpl.of(Type.CHAT_MODERATION_ACTIONS, toArray(channelId, credential.getUserId()), credential);
    }

    /**
     * Listening moderation actions on the own channel.
     */
    static Topic moderationActions(Credential credential) {
        return moderationActions(credential.getUserId(), credential);
    }

    /**
     * Listens EBS broadcast sent to specific extension on a specific channel
     */
    static Topic extensionBroadcast() {
        throw new UnsupportedOperationException("Extensions is currently unsupported");
    }

    /**
     * Listening live stream with view counter in specific channel name
     */
    static Topic videoPlayback(String channelName) {
        return TopicImpl.of(Type.VIDEO_PLAYBACK, toArray(channelName), null);
    }

    /**
     * Anyone makes a purchase on a channel.
     */
    static Topic commerce() {
        throw new UnsupportedOperationException("Commerce is currently unsupported");
    }

    static <S extends Serializable> String[] toArray(S... serialized) {
        return Arrays.stream(serialized).map(String::valueOf).toArray(String[]::new);
    }

    Type getType();

    String[] getSuffix();

    @Value.Lazy
    default UUID getCode() {
        return UUID.nameUUIDFromBytes(getRawType().getBytes(Charset.forName("UTF-8")));
    }

    @Value.Lazy
    default String getRawType() {
        return getType().toRaw(getSuffix());
    }

    @Nullable
    Credential getCredential();

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
         */
        CHANNEL_SUBSCRIPTION("channel-subscribe-events-v1"),

        /**
         * Anyone whispers the specified user.
         */
        WHISPERS("whispers"),

        /**
         * Anyone follow on a specified channel.
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

        String toRaw(String... subject) {
            return String.format("%s.%s", value, String.join(".", subject));
        }
    }
}
