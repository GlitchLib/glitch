package glitch.kraken.`object`.json.collections

import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Community

data class Communities(
        override val data: List<Community>
) : OrdinalList<Community>
