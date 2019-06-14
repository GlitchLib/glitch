package io.glitchlib.v5.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.BroadcasterType
import io.glitchlib.model.CreatedAndUpdatedAt
import io.glitchlib.model.IDObject
import io.glitchlib.model.SerializeTo
import io.glitchlib.v5.internal.model.json.AuthChannelImpl
import io.glitchlib.v5.internal.model.json.ChannelImpl
import java.util.Date
import java.util.Locale

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SerializeTo(ChannelImpl::class)
interface Channel : IDObject<Long>, CreatedAndUpdatedAt {
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
    val profileBannerBackgroundColor: String?
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
@SerializeTo(AuthChannelImpl::class)
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
data class ChannelFollow(
    @SerializedName("user")
    override val data: User,
    @SerializedName("notifications")
    override val hasNotifications: Boolean,
    override val createdAt: Date
) : Follow<User>

data class CommercialData(
    @SerializedName("Length")
    val length: Int,
    @SerializedName("Message")
    val message: String,
    @SerializedName("RetryAfter")
    val retryAfter: Int
)