package glitch.chat.irc

import glitch.api.objects.json.interfaces.IDObject
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class RoomState(
        override val id: Long,
        var broadcasterLanguage: Locale?,
        var isEmoteOnly: Boolean,
        var follow: Long,
        var isR9k: Boolean,
        var slow: Long,
        var isSubsOnly: Boolean
) : IDObject<Long> {
    val isFollowersOnly: Boolean
        get() = follow != -1L

    val isSlowMode: Boolean
        get() = slow > 0
}