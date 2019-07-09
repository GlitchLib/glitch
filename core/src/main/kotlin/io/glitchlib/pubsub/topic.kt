package io.glitchlib.pubsub

import io.glitchlib.Unofficial
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.auth.ScopeIsMissingException
import io.glitchlib.auth.Token
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

data class TopicInitializer(
    val type: TopicType,
    val suffix: Collection<String> = listOf(),
    val token: Token? = null
) {
    internal fun toTopic(credential: Credential?): Topic = Topics.from(type, credential, suffix)

    companion object {

        /**
         * Anyone cheers on a specified channel.
         */
        @JvmStatic
        fun bits(channelId: Long, credential: Token) =
            TopicInitializer(TopicType.CHANNEL_BITS, toList(channelId), credential)

        /**
         * Anyone cheers on a authorized channel.
         */
        @JvmStatic
        fun bits(credential: Token) =
            TopicInitializer(TopicType.CHANNEL_BITS, token = credential)

        /**
         * Anyone cheers on a specified channel.
         */
        @JvmStatic
        fun bitsV2(channelId: Long, credential: Token) =
            TopicInitializer(TopicType.CHANNEL_BITS_V2, toList(channelId), credential)

        /**
         * Anyone cheers on a authorized channel.
         */
        @JvmStatic
        fun bitsV2(credential: Token) = TopicInitializer(TopicType.CHANNEL_BITS_V2, token = credential)

        /**
         * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
         *
         *
         * Subgift subscription messages contain recipient information.
         */
        @JvmStatic
        fun subscription(channelId: Long, credential: Token) =
            TopicInitializer(TopicType.CHANNEL_SUBSCRIPTION, toList(channelId), credential)

        /**
         * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a authorized channel.
         *
         *
         * Subgift subscription messages contain recipient information.
         */
        @JvmStatic
        fun subscription(credential: Token) =
            TopicInitializer(TopicType.CHANNEL_SUBSCRIPTION, token = credential)

        /**
         * Anyone whispers the specified user.
         */
        @JvmStatic
        fun whispers(credential: Token) =
            TopicInitializer(TopicType.WHISPERS, token = credential)

        /**
         * Anyone follow on a specified channel.
         */
        @JvmStatic
        fun following(channelId: Long) =
            TopicInitializer(TopicType.CHANNEL_BITS, toList(channelId))

        /**
         * Listening moderation actions in specific channel.
         * Owner ID must be a moderator in specific channel.
         */
        @JvmStatic
        fun moderationActions(channelId: Long, credential: Token) =
            TopicInitializer(TopicType.CHAT_MODERATION_ACTIONS, toList(channelId), credential)

        /**
         * Listening moderation actions on the own channel.
         */
        @JvmStatic
        fun moderationActions(credential: Token) =
            TopicInitializer(TopicType.CHANNEL_BITS, token = credential)

        /**
         * Listens EBS broadcast sent to specific extension on a specific channel
         */
        @JvmStatic
        fun extensionBroadcast(): TopicInitializer =
            throw UnsupportedOperationException("Extensions is currently unsupported")

        /**
         * Listening live stream with view counter in specific channel name
         */
        @JvmStatic
        fun videoPlayback(channelName: String) =
            TopicInitializer(TopicType.CHANNEL_BITS, toList(channelName))

        /**
         * Anyone makes a purchase on a channel.
         */
        @JvmStatic
        fun commerce(channelId: Long, credential: Token) =
            TopicInitializer(TopicType.CHANNEL_BITS, toList(channelId), credential)

        /**
         * Anyone makes a purchase on your channel.
         */
        @JvmStatic
        fun commerce(credential: Token) =
            TopicInitializer(TopicType.CHANNEL_BITS, token = credential)

        private fun <S : Serializable> toList(vararg serialized: S): Collection<String> =
            serialized.map { it.toString() }
    }
}

object Topics {

    /**
     * Anyone cheers on a specified channel.
     */
    fun bits(channelId: Long, credential: Credential): Topic {
        return from(TopicType.CHANNEL_BITS, credential, channelId)
    }

    /**
     * Anyone cheers on a authorized channel.
     */
    fun bits(credential: Credential): Topic {
        return bits(credential.id, credential)
    }

    /**
     * Anyone cheers on a specified channel.
     */
    fun bitsV2(channelId: Long, credential: Credential): Topic {
        return if (credential.scopes.contains(Scope.BITS_READ)) {
            from(TopicType.CHANNEL_BITS_V2, credential, channelId)
        } else
            throw ScopeIsMissingException(Scope.BITS_READ)
    }

    /**
     * Anyone cheers on a authorized channel.
     */
    fun bitsV2(credential: Credential): Topic {
        return bitsV2(credential.id, credential)
    }

    /**
     * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
     *
     *
     * Subgift subscription messages contain recipient information.
     */
    @Throws(ScopeIsMissingException::class)
    fun subscription(channelId: Long, credential: Credential): Topic =
        from(TopicType.CHANNEL_SUBSCRIPTION, credential, channelId)

    /**
     * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a authorized channel.
     *
     *
     * Subgift subscription messages contain recipient information.
     */
    @Throws(ScopeIsMissingException::class)
    fun subscription(credential: Credential) = subscription(credential.id, credential)

    /**
     * Anyone whispers the specified user.
     */
    @Throws(ScopeIsMissingException::class)
    fun whispers(credential: Credential) = from(TopicType.WHISPERS, credential, credential.id)

    /**
     * Anyone follow on a specified channel.
     */
    fun following(channelId: Long): Topic {
        return from(TopicType.FOLLOW, null, channelId)
    }

    /**
     * Listening moderation actions in specific channel.
     * Owner ID must be a moderator in specific channel.
     */
    @Throws(ScopeIsMissingException::class)
    fun moderationActions(channelId: Long, credential: Credential): Topic {
        return if (credential.scopes.contains(Scope.CHAT_LOGIN) || credential.scopes.contains(Scope.CHANNEL_MODERATE)) {
            from(TopicType.CHAT_MODERATION_ACTIONS, credential, channelId, credential.id)
        } else
            throw ScopeIsMissingException(Scope.CHANNEL_MODERATE)
    }

    /**
     * Listening moderation actions on the own channel.
     */
    @Throws(ScopeIsMissingException::class)
    fun moderationActions(credential: Credential): Topic {
        return moderationActions(credential.id, credential)
    }

    /**
     * Listens EBS broadcast sent to specific extension on a specific channel
     */
    fun extensionBroadcast(): Topic {
        throw UnsupportedOperationException("Extensions is currently unsupported")
    }

    /**
     * Listening live stream with view counter in specific channel name
     */
    fun videoPlayback(channelName: String): Topic {
        return from(TopicType.VIDEO_PLAYBACK, null, channelName)
    }

    /**
     * Anyone makes a purchase on a channel.
     */
    fun commerce(channelId: Long, credential: Credential): Topic {
        return from(TopicType.CHANNEL_COMMERCE, credential, channelId)
    }

    /**
     * Anyone makes a purchase on your channel.
     */
    fun commerce(credential: Credential): Topic {
        return commerce(credential.id, credential)
    }

    fun <S : Serializable> from(type: TopicType, credential: Credential?, vararg suffix: S?) =
        from(type, credential, suffix.map { it.toString() })

    @Throws(ScopeIsMissingException::class)
    fun <S : Serializable> from(type: TopicType, credential: Credential?, suffix: Collection<S?>) =
        Topic(type, suffix.mapNotNull { it?.toString() }, credential)
}

data class Topic internal constructor(
    val type: TopicType,
    val suffix: Collection<String>,
    val credential: Credential? = null
) {
    init {
        if (credential != null && !type.hasRequirement(credential.scopes))
            type.requiredScope.firstOrNull { !credential.scopes.contains(it) }.let {
                if (it != null) throw ScopeIsMissingException(
                    it
                ) else Unit
            }
    }

    val rawType
        get() = type.toRaw(*suffix.toTypedArray())

    val code: UUID
        get() = UUID.nameUUIDFromBytes(
            "$rawType/${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH).let {
                it.timeZone = TimeZone.getTimeZone("UTC")
                it.format(Date())
            }}".toByteArray(Charsets.UTF_8)
        )

    override fun toString(): String = "Topic: $rawType"
}

enum class TopicType(
    internal val prefix: String,
    vararg scope: Scope
) {
    /**
     * Anyone cheers on a specified channel.
     */
    CHANNEL_BITS("channel-bits-events-v1"),

    /**
     * Anyone cheers on a specified channel.
     */
    CHANNEL_BITS_V2("channel-bits-events-v2", Scope.BITS_READ),

    /**
     * Message sent when a user earns a new Bits badge in a particular channel, and chooses to share the notification with chat.
     */
    CHANNEL_BITS_BADGE_UNLOCK("channel-bits-badge-unlocks"),

    /**
     * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
     *
     * Subgift subscription messages contain recipient information.
     * Formatting:
     */
    CHANNEL_SUBSCRIPTION("channel-subscribe-events-v1", Scope.CHANNEL_SUBSCRIPTIONS),

    /**
     * Anyone whispers the specified user.
     */
    WHISPERS("whispers", Scope.WHISPERS_READ, Scope.CHAT_LOGIN),

    /**
     * Anyone follow on a specified channel.
     * Formatting: `{&quot;display_name&quot;:&quot;&lt;display name&gt;&quot;, &quot;username&quot;:&quot;&lt;username&gt;&quot;, &quot;user_id&quot;:&quot;&lt;id&gt;&quot;}`
     */
    @Unofficial("[Unknown]") // TODO: Required Source
    FOLLOW("following"),

    /**
     * Listening moderation actions in specific channel.
     * Owner ID must be a moderator in specific channel.
     */
    @Unofficial("https://discuss.dev.twitch.tv/t/in-line-broadcaster-chat-mod-logs/7281")
    CHAT_MODERATION_ACTIONS("chat_moderator_actions", Scope.CHANNEL_MODERATE, Scope.CHAT_LOGIN),

    /**
     * Listens EBS broadcast sent to specific extension on a specific channel
     */
    @Unofficial("https://discuss.dev.twitch.tv/t/private-topic-for-extension-events-in-pubsub/15628/3")
    CHANNEL_EXTENSION_BROADCAST("channel-ext-v1") {
        override fun toRaw(vararg subject: String): String {
            return super.toRaw(*subject) + "-broadcast"
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

    internal val requiredScope: Collection<Scope> = scope.toList()

    internal open fun toRaw(vararg subject: String) = "$prefix.${subject.joinToString(".")}"

    internal fun hasRequirement(scope: Collection<Scope>) =
        if (requiredScope.isNotEmpty()) scope.any { s -> requiredScope.any { s == it } } else scope.isNotEmpty()
}