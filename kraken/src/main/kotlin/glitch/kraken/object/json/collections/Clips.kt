package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.CursorList
import glitch.kraken.`object`.json.Clip

data class Clips(
        override val data: List<Clip>,
        @SerializedName("_cursor")
        override val cursor: String
) : CursorList<Clip>
