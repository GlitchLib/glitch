package glitch.pubsub;

import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class Topics {

    /**
     * Anyone cheers on a specified channel.
     */
    public static Topic bits(Long channelId, Credential credential) {
        return new Topic(Topic.Type.CHANNEL_BITS, toArray(channelId), credential);
    }

    /**
     * Anyone cheers on a authorized channel.
     */
    public static Topic bits(Credential credential) {
        return bits(credential.getUserId(), credential);
    }

    /**
     * Anyone cheers on a specified channel.
     */
    public static Topic bitsV2(Long channelId, Credential credential) {
        if (credential.getScopes().contains(GlitchScope.BITS_READ)) {
            return new Topic(Topic.Type.CHANNEL_BITS_V2, toArray(channelId), credential);
        } else throw new ScopeIsMissingException(GlitchScope.BITS_READ);
    }

    /**
     * Anyone cheers on a authorized channel.
     */
    public static Topic bitsV2(Credential credential) {
        return bitsV2(credential.getUserId(), credential);
    }

    /**
     * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
     * <p>
     * Subgift subscription messages contain recipient information.
     */
    public static Topic subscription(Long channelId, Credential credential) throws ScopeIsMissingException {
        if (credential.getScopes().contains(GlitchScope.CHANNEL_SUBSCRIPTIONS)) {
            return new Topic(Topic.Type.CHANNEL_SUBSCRIPTION, toArray(channelId), credential);
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
            return new Topic(Topic.Type.WHISPERS, toArray(credential.getUserId()), credential);
        } else throw new ScopeIsMissingException(GlitchScope.WHISPERS_READ);
    }

    /**
     * Anyone follow on a specified channel.
     */
    public static Topic following(Long channelId) {
        return new Topic(Topic.Type.FOLLOW, toArray(channelId), null);
    }

    /**
     * Listening moderation actions in specific channel.
     * Owner ID must be a moderator in specific channel.
     */
    public static Topic moderationActions(Long channelId, Credential credential) throws ScopeIsMissingException {
        if (credential.getScopes().contains(GlitchScope.CHAT_LOGIN) || credential.getScopes().contains(GlitchScope.CHANNEL_MODERATE)) {
            return new Topic(Topic.Type.CHAT_MODERATION_ACTIONS, toArray(channelId, credential.getUserId()), credential);
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
        return new Topic(Topic.Type.VIDEO_PLAYBACK, toArray(channelName), null);
    }

    /**
     * Anyone makes a purchase on a channel.
     */
    public static Topic commerce(Long channelId, Credential credential) {
        return new Topic(Topic.Type.CHANNEL_COMMERCE, toArray(channelId), credential);
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

        if (split.length > 2 && split[0].equals(Topic.Type.CHAT_MODERATION_ACTIONS.value)) {
            return new Topic(Topic.Type.CHAT_MODERATION_ACTIONS, suffix, null);
        } else {
            return new Topic(Topic.Type.readType(split[0]), suffix, null);
        }
    }
}
