package glitch.chat.events

import glitch.api.objects.enums.UserType
import glitch.api.objects.json.Badge
import java.awt.Color

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface IChannel {
    val channelName: String
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface IUser {
    val username: String
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface IChannelUser : IChannel, IUser

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface IDChannelUser : IDChannel, IDUser

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface IDUser {
    val userId: Long
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface IDChannel {
    val channelId: Long
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface GlobalUserState {
    val badges: Set<Badge>
    val color: Color
    val userType: UserType
    val displayName: String

    val hasPrime: Boolean
        get() = badges.map { it.name == "premium" }.firstOrNull { it } ?: false
    val hasTurbo: Boolean
        get() = badges.map { it.name == "turbo" }.firstOrNull { it } ?: false
    val isTwitchAdmin: Boolean
        get() = badges.map { it.name == "admin" }.firstOrNull { it } ?: false
    val isTwitchStaff: Boolean
        get() = badges.map { it.name == "staff" }.firstOrNull { it } ?: false
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface ChannelUserState : GlobalUserState {
    val isMod: Boolean
        get() = badges.map { it.name == "moderator" }.firstOrNull { it } ?: false
    val isSubscriber: Boolean
        get() = badges.map { it.name == "subscriber" }.firstOrNull { it } ?: false
    val isVip: Boolean
        get() = badges.map { it.name == "vip" }.firstOrNull { it } ?: false
    val isBroadcaster: Boolean
        get() = badges.map { it.name == "broadcaster" }.firstOrNull { it } ?: false
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface IMessage : IDUser, IUser {
    val message: String?
}

// TODO: CHAT ROOMS
///**
// *
// * @author Damian Staszewski [damian@stachuofficial.tv]
// * @version %I%, %G%
// * @since 1.0
// */
//interface IChatRoom : IChannel {
//    val room: ChatRoomEntity
//    override val channel: ChannelEntity
//        get() = room.channel
//}
//
///**
// *
// * @author Damian Staszewski [damian@stachuofficial.tv]
// * @version %I%, %G%
// * @since 1.0
// */
//interface IChatRoomUser : IChatRoom {
//    val user: ChatRoomUserEntity
//    val userDirect: UserEntity
//}
