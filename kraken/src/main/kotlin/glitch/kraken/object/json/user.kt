package glitch.kraken.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.adapters.SerializeTo
import glitch.api.objects.enums.UserType
import glitch.api.objects.json.interfaces.Creation
import glitch.api.objects.json.interfaces.IDObject
import glitch.api.objects.json.interfaces.Updated
import glitch.kraken.`object`.json.impl.AuthUserImpl
import glitch.kraken.`object`.json.impl.UserImpl
import java.time.Instant

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SerializeTo(UserImpl::class)
interface User : IDObject<Long>, Creation, Updated {
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
data class UserChannelFollow(
        @SerializedName("channel")
        override val data: Channel,
        @SerializedName("notifications")
        override val hasNotifications: Boolean,
        override val createdAt: Instant
) : Follow<Channel>