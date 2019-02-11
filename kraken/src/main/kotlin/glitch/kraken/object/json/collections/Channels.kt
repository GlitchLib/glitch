package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Channel

data class Channels(
        override val data: List<Channel>,
        @SerializedName("_total")
        override val size: Int
) : OrdinalList<Channel>
