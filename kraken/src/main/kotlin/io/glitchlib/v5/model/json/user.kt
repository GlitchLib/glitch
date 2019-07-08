package io.glitchlib.v5.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.CreatedAndUpdatedAt
import io.glitchlib.model.IDObject
import io.glitchlib.model.SerializeTo
import io.glitchlib.model.UserType
import io.glitchlib.v5.internal.model.json.AuthUserImpl
import io.glitchlib.v5.internal.model.json.UserImpl
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SerializeTo(UserImpl::class)
interface User : IDObject<Long>, CreatedAndUpdatedAt {
    @get:SerializedName(value = "bio", alternate = ["description"])
    val userBio: String
    val displayName: String
    val logo: String
    @get:SerializedName(value = "username", alternate = ["login", "name"])
    val username: String
    @get:SerializedName(value = "type", alternate = ["user_type"])
    val userType: UserType
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SerializeTo(AuthUserImpl::class)
interface AuthUser : User {
    val email: String
    @get:SerializedName("email_verified")
    val isEmailVerified: Boolean
    @get:SerializedName("notifications.push")
    val isPushNotifications: Boolean
    @get:SerializedName("notifications.email")
    val isEmailNotifications: Boolean
    @get:SerializedName("partnered")
    val isPartnered: Boolean
    @get:SerializedName("twitter_connected")
    val isTwitterConnected: Boolean
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UserFollow(
        @SerializedName("channel")
        override val data: Channel,
        @SerializedName("notifications")
        override val hasNotifications: Boolean,
        override val createdAt: Date
) : Follow<Channel>