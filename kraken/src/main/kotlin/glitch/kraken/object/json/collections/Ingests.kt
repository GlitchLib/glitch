package glitch.kraken.`object`.json.collections

import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Ingest

data class Ingests(
        override val data: List<Ingest>
) : OrdinalList<Ingest>
