package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Subscriber

data class Subscribers(
        override val data: List<Subscriber>,
        @SerializedName("total")
        override val size: Int = 0
) : OrdinalList<Subscriber>
