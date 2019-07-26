package io.glitchlib.tmi.irc

import io.glitchlib.model.IDObject

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

    fun getEmoteUrl(emoteSize: Size): String =
        "http://static-cdn.jtvnw.net/emoticons/v1/$id/${emoteSize.value}"

    enum class Size(internal val value: Double) {
        X1(1.0),
        X2(2.0),
        X3(3.0)
    }
}