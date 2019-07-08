package io.glitchlib.v5.model.json

import com.google.common.collect.Multimap
import java.util.*

class EmoteSets(
        val emoticonSets: Multimap<Int, Emote>
) {

    fun toEmoteSets() = emoticonSets.asMap()
            .map { e -> EmoteSet(e.key, ArrayList(e.value)) }
            .toSet()

    operator fun get(id: Int) =
            toEmoteSets().firstOrNull { it.id == id }
}
