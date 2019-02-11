package glitch.chat.events

import glitch.api.objects.enums.UserType
import glitch.api.objects.json.Badge
import glitch.api.objects.json.interfaces.IDObject
import glitch.api.ws.events.IEvent
import glitch.chat.GlitchChat
import java.awt.Color
import java.time.Instant
import java.util.*

/**
 * User has been joined to channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UserJoinedChannelEvent(
        override val client: GlitchChat,
        override val channelName: String,
        override val username: String
) : IChannelUser, IEvent<GlitchChat>

/**
 * User has been left channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UserLeavingChannelEvent(
        override val client: GlitchChat,
        override val channelName: String,
        override val username: String
) : IChannelUser, IEvent<GlitchChat>

/**
 * Received message from channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelMessageEvent(
        override val client: GlitchChat,
        override val id: UUID,
        override val channelId: Long,
        override val userId: Long,
        override val badges: Set<Badge>,
        override val color: Color,
        override val userType: UserType,
        override val displayName: String,
        override val channelName: String,
        override val username: String,
        override val message: String?,
        override val createdAt: Instant,
        val isActionMessage: Boolean = false
) : IMessage, IChannelUser, ChannelUserState, IEvent<GlitchChat>, IDObject<UUID>, IDChannel


/**
 * Received message from channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelMessageDeletedEvent(
        override val client: GlitchChat,
        override val id: UUID,
        override val channelName: String,
        override val userId: Long,
        override val username: String,
        override val message: String?,
        val isActionMessage: Boolean = false
) : IMessage, IChannelUser, IEvent<GlitchChat>, IDObject<UUID>

/**
 * Received message with bits from channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelBitsMessageEvent(
        override val client: GlitchChat,
        override val id: UUID,
        override val channelId: Long,
        override val userId: Long,
        override val badges: Set<Badge>,
        override val color: Color,
        override val userType: UserType,
        override val displayName: String,
        override val channelName: String,
        override val username: String,
        override val message: String?,
        override val createdAt: Instant,
        val bits: Int,
        val isActionMessage: Boolean = false
) : IMessage, IChannelUser, ChannelUserState, IDChannel, IEvent<GlitchChat>, IDObject<UUID>


/**
 * Someone subscribe channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class SubscriptionEvent(
        override val client: GlitchChat,
        override val badges: Set<Badge>,
        override val color: Color,
        override val userType: UserType,
        override val displayName: String,
        override val channelName: String,
        override val username: String,
        val continuouslyMonths: Int?,
        val months: Int = 1,
        val message: Optional<String> = Optional.empty()
) : IChannelUser, ChannelUserState, IEvent<GlitchChat>

/**
 * Someone gift subscription on the channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class GiftSubEvent(
        override val client: GlitchChat,
        override val badges: Set<Badge>,
        override val color: Color,
        override val userType: UserType,
        override val displayName: String,
        override val channelName: String,
        override val username: String,
        val recipientUsername: String
) : IChannelUser, ChannelUserState, IEvent<GlitchChat>

/**
 * Anonymous user gift a subscription on the channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class AnonymousGiftSubEvent(
        override val client: GlitchChat,
        override val badges: Set<Badge>,
        override val color: Color,
        override val userType: UserType,
        override val channelName: String,
        val recipientUsername: String
) : IChannel, ChannelUserState, IEvent<GlitchChat> {
    override val displayName = ""
}


/**
 * Anonymous user gift a subscription on the channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelStateEvent(
        override val client: GlitchChat,
        override val channelName: String,
        override val channelId: Long,
        val broadcasterLanguage: Locale?,
        val isEmoteOnly: Boolean,
        val follows: Int,
        val isR9k: Boolean,
        val slow: Int,
        val isSubOnly: Boolean
) : IEvent<GlitchChat>, IChannel, IDChannel {
    val isFollows = follows > -1
    val isSlow = slow > 0
}

data class ChannelStateChangedEvent(
        override val client: GlitchChat,
        override val channelName: String,
        override val channelId: Long,
        val key: String,
        val value: String?
) : IEvent<GlitchChat>, IChannel, IDChannel