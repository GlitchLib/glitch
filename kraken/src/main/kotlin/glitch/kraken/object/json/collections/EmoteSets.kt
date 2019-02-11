package glitch.kraken.`object`.json.collections

import glitch.kraken.`object`.json.Emote
import glitch.kraken.`object`.json.EmoteSet
import org.apache.commons.collections4.MultiValuedMap
import java.util.*

class EmoteSets(
        val emoticonSets: MultiValuedMap<Int, Emote>
) {

    fun toEmoteSets() = emoticonSets.asMap()
            .map { e -> EmoteSet(e.key, ArrayList(e.value)) }
            .toSet()

    operator fun get(id: Int) =
            toEmoteSets().firstOrNull { (id1) -> id1 == id }
}
