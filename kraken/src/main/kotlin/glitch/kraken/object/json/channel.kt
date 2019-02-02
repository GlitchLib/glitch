package glitch.kraken.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.adapters.SerializeTo
import glitch.api.objects.enums.BroadcasterType
import glitch.api.objects.json.interfaces.Creation
import glitch.api.objects.json.interfaces.IDObject
import glitch.api.objects.json.interfaces.Updated
import glitch.kraken.`object`.json.impl.ChannelImpl
import sun.net.www.protocol.http.AuthCacheImpl
import java.awt.Color
import java.time.Instant
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SerializeTo(ChannelImpl::class)
interface Channel : IDObject<Long>, Creation, Updated {
    @get:SerializedName(value = "username", alternate = ["login", "name"])
    val username: String
    @get:SerializedName("status")
    val title: String
    val broadcasterLanguage: Locale
    val displayName: String
    val followers: Long
    val game: String
    val language: Locale
    val logo: String
    val mature: Boolean
    val partner: Boolean
    val profileBanner: String?
    val profileBannerBackgroundColor: Color?
    val url: String
    val videoBanner: String?
    val views: Long
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SerializeTo(AuthCacheImpl::class)
interface AuthChannel : Channel {
    val broadcasterType: BroadcasterType
    val email: String
    val streamKey: String
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelUserFollow(
        @SerializedName("user")
        override val data: User,
        @SerializedName("notifications")
        override val hasNotifications: Boolean,
        override val createdAt: Instant
) : Follow<User>

data class CommercialData(
        @SerializedName("Length")
        val length: Int,
        @SerializedName("Message")
        val message: String,
        @SerializedName("RetryAfter")
        val retryAfter: Int
)