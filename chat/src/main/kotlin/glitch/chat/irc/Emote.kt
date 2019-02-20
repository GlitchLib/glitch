package glitch.chat.irc

import glitch.api.objects.json.interfaces.IDObject

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 0.1.0
 */
data class Emote(
        override val id: Int,
        val indexRanges: Set<Pair<Int, Int>>
) : IDObject<Int> {

    fun getEmoteUrl(emoteSize: Emote.Size): String {
        return String.format("http://static-cdn.jtvnw.net/emoticons/v1/%d/%s", id, emoteSize.value)
    }

    enum class Size private constructor(internal val value: Double) {
        X1(1.0),
        X2(2.0),
        X3(3.0)
    }
}