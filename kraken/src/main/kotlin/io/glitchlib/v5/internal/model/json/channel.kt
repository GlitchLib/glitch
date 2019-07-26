package io.glitchlib.v5.internal.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.BroadcasterType
import io.glitchlib.v5.model.json.AuthChannel
import io.glitchlib.v5.model.json.Channel
import java.util.Date
import java.util.Locale

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
    override val createdAt: Date,
    override val updatedAt: Date,
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
    override val profileBannerBackgroundColor: String,
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
    override val createdAt: Date,
    override val updatedAt: Date,
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
    override val profileBannerBackgroundColor: String,
    override val url: String,
    override val views: Long,
    override val followers: Long,
    override val broadcasterType: BroadcasterType,
    override val email: String,
    override val streamKey: String
) : AuthChannel