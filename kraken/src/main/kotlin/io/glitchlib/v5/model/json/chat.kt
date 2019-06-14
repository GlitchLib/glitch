package io.glitchlib.v5.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.IDObject

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class EmoteSet(
    override val id: Int,
    val emotes: List<Emote>
) : IDObject<Int> {
    fun getByName(name: String): Emote? {
        return emotes.firstOrNull { e -> e.name == name }
    }

    fun getById(id: Long?): Emote? {
        return emotes.firstOrNull { e -> e.id == id }
    }
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Emote(
    override val id: Long,
    @SerializedName("regex", alternate = ["code"])
    val name: String,
    val images: List<EmoteImage>
) : IDObject<Long>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class EmoteImage(
    val width: Int,
    val height: Int,
    val url: String,
    @SerializedName("emoticon_set")
    val emoteSet: Long
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChatBadges(
    val admin: ChatBadgesImage,
    val broadcaster: ChatBadgesImage,
    @Deprecated("https://blog.twitch.tv/thank-you-global-moderators-4d44cfccf22")
    val globalMod: ChatBadgesImage,
    val mod: ChatBadgesImage,
    val staff: ChatBadgesImage,
    val subscriber: ChatBadgesImage,
    val turbo: ChatBadgesImage
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChatBadgesImage(
    val alpha: String,
    val image: String,
    val svg: String
)