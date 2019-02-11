package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.CursorList
import glitch.kraken.`object`.json.UserChannelFollow

data class UserChannelFollows(
        override val data: List<UserChannelFollow>,
        @SerializedName("_cursor")
        override val cursor: String,
        @SerializedName("_total")
        override val size: Int
) : CursorList<UserChannelFollow>
