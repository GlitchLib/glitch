package glitch.kraken.`object`.json.impl

import glitch.api.objects.enums.UserType
import glitch.kraken.`object`.json.AuthUser
import glitch.kraken.`object`.json.User
import java.time.Instant

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UserImpl(
        override val userBio: String,
        override val displayName: String,
        override val logo: String,
        override val username: String,
        override val userType: UserType,
        override val id: Long,
        override val createdAt: Instant,
        override val updatedAt: Instant
) : User

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class AuthUserImpl(
        override val userBio: String,
        override val displayName: String,
        override val logo: String,
        override val username: String,
        override val userType: UserType,
        override val id: Long,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        override val email: String,
        override val isEmailVerified: Boolean,
        override val isPushNotifications: Boolean,
        override val isEmailNotifications: Boolean,
        override val isPartnered: Boolean,
        override val isTwitterConnected: Boolean
) : AuthUser