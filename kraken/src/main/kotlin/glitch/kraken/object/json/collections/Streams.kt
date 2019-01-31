package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Stream

data class Streams(
        override val data: List<Stream>,
        @SerializedName("_total")
        override val size: Int
) : OrdinalList<Stream>
