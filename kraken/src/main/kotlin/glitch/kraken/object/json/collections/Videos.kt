package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.Video

data class Videos(
        override val data: List<Video>,
        @SerializedName("total")
        override val size: Int = data.size
) : OrdinalList<Video>