package glitch.kraken.`object`.json.collections

import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.FeatureStream

data class FeatureStreams(
        override val data: List<FeatureStream>
) : OrdinalList<FeatureStream>
