package glitch.kraken.`object`.json.collections

import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Emote

data class Emotes(
        override val data: List<Emote>
) : OrdinalList<Emote>
