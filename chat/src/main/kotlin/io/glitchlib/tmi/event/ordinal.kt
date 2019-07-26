package io.glitchlib.tmi.event

import io.glitchlib.GlitchClient
import io.glitchlib.model.Badge
import io.glitchlib.model.IDObject
import io.glitchlib.model.IEvent
import io.glitchlib.model.UserType
import java.awt.Color
import java.util.Date

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class PrivateMessageEvent(
    override val client: GlitchClient,
    override val id: Int,
    override val badges: Set<Badge>,
    override val color: Color,
    override val username: String,
    override val displayName: String,
    override val userId: Long,
    override val userType: UserType,
    override val message: String?,
    override val createdAt: Date
) : IUser, IMessage, GlobalUserState, IEvent, IDObject<Int> {
//    fun reply(message: String) = user.send(message)
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class GlobalUserStateEvent(
    override val client: GlitchClient,
    override val badges: Set<Badge>,
    override val color: Color,
    override val userType: UserType,
    override val displayName: String
) : GlobalUserState, IEvent


