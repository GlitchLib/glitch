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
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class PrivateMessageEvent(
        override val client: GlitchChat,
        override val id: Int,
        override val badges: Set<Badge>,
        override val color: Color,
        override val username: String,
        override val displayName: String,
        override val userId: Long,
        override val userType: UserType,
        override val message: String?,
        override val createdAt: Instant
) : IUser, IMessage, GlobalUserState, IEvent<GlitchChat>, IDObject<Int> {
//    fun reply(message: String) = user.send(message)
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class GlobalUserStateEvent(
        override val badges: Set<Badge>,
        override val color: Color,
        override val userType: UserType,
        override val displayName: String
) : GlobalUserState


