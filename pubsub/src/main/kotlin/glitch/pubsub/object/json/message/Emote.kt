package glitch.pubsub.`object`.json.message

import glitch.api.objects.json.interfaces.IDObject

data class Emote(override val id: Long, val start: Int, val end: Int) : IDObject<Long> {

    fun getEmoteUrl(emoteSize: Size): String {
        return String.format("http://static-cdn.jtvnw.net/emoticons/v1/%d/%s", id, emoteSize.value)
    }

    enum class Size private constructor(internal val value: Double) {
        X1(1.0),
        X2(2.0),
        X3(3.0)
    }
}
