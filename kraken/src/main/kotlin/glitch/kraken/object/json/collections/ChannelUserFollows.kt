package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.ChannelUserFollow

data class ChannelUserFollows(
        override val data: List<ChannelUserFollow>,
        @SerializedName("_total")
        override val size: Int
) : OrdinalList<ChannelUserFollow>
