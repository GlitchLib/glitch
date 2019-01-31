package glitch.kraken.`object`.json.impl

import com.google.gson.annotations.SerializedName
import glitch.api.objects.enums.BroadcasterType
import glitch.kraken.`object`.json.AuthChannel
import glitch.kraken.`object`.json.Channel
import java.awt.Color
import java.time.Instant
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelImpl(
        @SerializedName(value = "id", alternate = ["_id"])
        override val id: Long,
        @SerializedName(value = "username", alternate = ["login", "name"])
        override val username: String,
        override val displayName: String,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        override val logo: String,
        override val mature: Boolean,
        @SerializedName("status")
        override val title: String,
        override val broadcasterLanguage: Locale,
        override val game: String,
        override val language: Locale,
        override val partner: Boolean,
        override val videoBanner: String,
        override val profileBanner: String,
        override val profileBannerBackgroundColor: Color,
        override val url: String,
        override val views: Long,
        override val followers: Long
) : Channel

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class AuthChannelImpl(
        @SerializedName(value = "id", alternate = ["_id"])
        override val id: Long,
        @SerializedName(value = "username", alternate = ["login", "name"])
        override val username: String,
        override val displayName: String,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        override val logo: String,
        override val mature: Boolean,
        @SerializedName("status")
        override val title: String,
        override val broadcasterLanguage: Locale,
        override val game: String,
        override val language: Locale,
        override val partner: Boolean,
        override val videoBanner: String,
        override val profileBanner: String,
        override val profileBannerBackgroundColor: Color,
        override val url: String,
        override val views: Long,
        override val followers: Long,
        override val broadcasterType: BroadcasterType,
        override val email: String,
        override val streamKey: String
) : AuthChannel