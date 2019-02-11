package glitch.kraken.`object`.json.collections

import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Team

data class Teams(
        override val data: List<Team>
) : OrdinalList<Team>
