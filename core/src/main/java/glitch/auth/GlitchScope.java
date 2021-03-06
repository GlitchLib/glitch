package glitch.auth;

public enum GlitchScope {

    // --- Helix

    /**
     * View analytics data for your extensions.
     */
    ANALYTICS_READ_EXTENSION("analytics:read:extensions"),
    /**
     * View analytics data for your games.
     */
    ANALYTICS_READ_GAMES("analytics:read:games"),
    /**
     * View bits information for your channel.
     */
    BITS_READ("bits:read"),
    /**
     * Manage a clip object.
     */
    CLIPS_EDIT("clips:edit"),
    /**
     * Manage a user object.
     */
    USER_EDIT("user:edit"),
    /**
     * Edit your channel’s broadcast configuration, including extension configuration. (This scope implies {@link #USER_READ_BROADCAST {@code user:read:broadcast}} capability.)
     */
    USER_EDIT_BROADCAST("user:edit:broadcast"),
    /**
     * View your broadcasting configuration, including extension configurations.
     */
    USER_READ_BROADCAST("user:read:broadcast"),
    /**
     * Read authorized user’s email address.
     */
    USER_READ_EMAIL("user:read:email"),

    // --- Kraken

    /**
     * Read whether a user is subscribed to your channel.
     */
    CHANNEL_CHECK_SUBSCRIPTION,
    /**
     * Trigger commercials on channel.
     */
    CHANNEL_COMMERCIAL,
    /**
     * Write channel metadata (game, status, etc).
     */
    CHANNEL_EDITOR,
    /**
     * Add posts and reactions to a channel feed.
     *
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/how-the-removal-of-channel-feed-and-pulse-affects-the-twitch-api-v5/16540">Twitch removes Channel Feed and Pulse.</a>
     */
    @Deprecated
    CHANNEL_FEED_EDIT,
    /**
     * View a channel feed.
     *
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/how-the-removal-of-channel-feed-and-pulse-affects-the-twitch-api-v5/16540">Twitch removes Channel Feed and Pulse.</a>
     */
    @Deprecated
    CHANNEL_FEED_READ,
    /**
     * Read nonpublic channel information, including email address and stream key.
     */
    CHANNEL_READ,
    /**
     * Reset a channel’s stream key.
     */
    CHANNEL_STREAM,
    /**
     * Read all subscribers to your channel.
     */
    CHANNEL_SUBSCRIPTIONS,
    /**
     * Log into chat and send messages.
     *
     * @see #CHAT_EDIT
     * @see #CHAT_READ
     * @see #CHANNEL_MODERATE
     * @see #WHISPERS_EDIT
     * @see #WHISPERS_READ
     * @deprecated cannot be requested by new clients.
     */
    @Deprecated
    CHAT_LOGIN,
    /**
     * Manage a user’s collections (of videos).
     */
    COLLECTIONS_EDIT,
    /**
     * Manage a user’s communities.
     */
    COMMUNITIES_EDIT,
    /**
     * Manage community moderators.
     */
    COMMUNITIES_MODERATE,
    /**
     * Use OpenID Connect authentication.
     */
    OPENID,
    /**
     * Turn on/off ignoring a user. Ignoring users means you cannot see them type, receive messages of them, etc.
     */
    USER_BLOCKS_EDIT,
    /**
     * Read a user’s list of ignored users.
     */
    USER_BLOCKS_READ,
    /**
     * Manage a user’s followed channels.
     */
    USER_FOLLOWS_EDIT,
    /**
     * Read nonpublic user information, like email address.
     */
    USER_READ,
    /**
     * Read a user’s subscriptions.
     */
    USER_SUBSCRIPTIONS,
    /**
     * Turn on Viewer Heartbeat Service ability to record user data.
     */
    VIEWING_ACTIVITY_READ,

    // --- Chat and PubSub

    /**
     * Perform moderation actions in a channel.
     */
    CHANNEL_MODERATE("channel:moderate"),
    /**
     * Send live stream chat and rooms messages.
     */
    CHAT_EDIT("chat:edit"),
    /**
     * View live stream chat and rooms messages.
     */
    CHAT_READ("chat:read"),
    /**
     * View your whisper messages.
     */
    WHISPERS_READ("whispers:read"),
    /**
     * Send whisper messages.
     */
    WHISPERS_EDIT("whispers:edit");

    private final String value;

    GlitchScope(String value) {
        this.value = value;
    }

    GlitchScope() {
        this.value = name().toLowerCase();
    }

    public static GlitchScope of(String name) {
        for (GlitchScope scope : values()) {
            if (scope.value.equals(name)) {
                return scope;
            }
        }
        return null;
    }

    /**
     * Get the identifier that oauth will recognize.
     *
     * @return A `{@link String}` identifier of the scope.
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
