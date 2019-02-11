package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Game

data class Games(
        override val data: List<Game>,
        @SerializedName("_total")
        override val size: Int = data.size
) : OrdinalList<Game>