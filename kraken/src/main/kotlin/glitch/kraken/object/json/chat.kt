package glitch.kraken.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.http.Unofficial
import glitch.api.objects.json.Badge
import glitch.api.objects.json.interfaces.IDObject
import java.awt.Color
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
enum class Role {
    EVERYONE,
    SUBSCRIBER,
    MODERATOR
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChatRoom(
        override val id: UUID,
        val ownerId: Long,
        val name: String,
        val topic: String,
        val isPreviewable: Boolean,
        val minimumAllowedRole: Role
) : IDObject<UUID>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@Unofficial
data class GlobalUserState(
        override val id: Long,
        val login: String,
        val displayName: String,
        val color: Color,
        val isVerifiedBot: Boolean,
        val isKnownBot: Boolean,
        val badges: Set<Badge>
) : IDObject<Long>

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