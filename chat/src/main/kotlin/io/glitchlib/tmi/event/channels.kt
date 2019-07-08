package io.glitchlib.tmi.event

import io.glitchlib.GlitchClient
import io.glitchlib.model.Badge
import io.glitchlib.model.IDObject
import io.glitchlib.model.IEvent
import io.glitchlib.model.UserType
import java.awt.Color
import java.util.Date
import java.util.Locale
import java.util.Optional
import java.util.UUID

/**
 * User has been joined to channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UserJoinedChannelEvent(
    override val client: GlitchClient,
    override val channelName: String,
    override val username: String
) : IChannelUser, IEvent

/**
 * User has been left channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UserLeavingChannelEvent(
    override val client: GlitchClient,
    override val channelName: String,
    override val username: String
) : IChannelUser, IEvent

/**
 * Received message from channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelMessageEvent(
    override val client: GlitchClient,
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
    override val createdAt: Date,
    val isActionMessage: Boolean = false
) : IMessage, IChannelUser, ChannelUserState, IEvent, IDObject<UUID>, IDChannel


/**
 * Received message from channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelMessageDeletedEvent(
    override val client: GlitchClient,
    override val id: UUID,
    override val channelName: String,
    override val userId: Long,
    override val username: String,
    override val message: String?,
    val isActionMessage: Boolean = false
) : IMessage, IChannelUser, IEvent, IDObject<UUID>

/**
 * Received message with bits from channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelBitsMessageEvent(
    override val client: GlitchClient,
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
    override val createdAt: Date,
    val bits: Int,
    val isActionMessage: Boolean = false
) : IMessage, IChannelUser, ChannelUserState, IDChannel, IEvent, IDObject<UUID>


/**
 * Someone subscribe channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class SubscriptionEvent(
    override val client: GlitchClient,
    override val badges: Set<Badge>,
    override val color: Color,
    override val userType: UserType,
    override val displayName: String,
    override val channelName: String,
    override val username: String,
    val continuouslyMonths: Int?,
    val months: Int = 1,
    val message: Optional<String> = Optional.empty()
) : IChannelUser, ChannelUserState, IEvent

/**
 * Someone gift subscription on the channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class GiftSubEvent(
    override val client: GlitchClient,
    override val badges: Set<Badge>,
    override val color: Color,
    override val userType: UserType,
    override val displayName: String,
    override val channelName: String,
    override val username: String,
    val recipientUsername: String
) : IChannelUser, ChannelUserState, IEvent

/**
 * Anonymous user gift a subscription on the channel
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class AnonymousGiftSubEvent(
    override val client: GlitchClient,
    override val badges: Set<Badge>,
    override val color: Color,
    override val userType: UserType,
    override val channelName: String,
    val recipientUsername: String
) : IChannel, ChannelUserState, IEvent {
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
    override val client: GlitchClient,
    override val channelName: String,
    override val channelId: Long,
    val broadcasterLanguage: Locale?,
    val isEmoteOnly: Boolean,
    val follows: Int,
    val isR9k: Boolean,
    val slow: Int,
    val isSubOnly: Boolean
) : IEvent, IChannel, IDChannel {
    val isFollows = follows > -1
    val isSlow = slow > 0
}

data class ChannelStateChangedEvent(
    override val client: GlitchClient,
    override val channelName: String,
    override val channelId: Long,
    val key: String,
    val value: String?
) : IEvent, IChannel, IDChannel