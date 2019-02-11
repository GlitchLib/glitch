package glitch.kraken.`object`.json.collections

import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Cheermote

data class Cheermotes(
        override val data: List<Cheermote>
) : OrdinalList<Cheermote>
