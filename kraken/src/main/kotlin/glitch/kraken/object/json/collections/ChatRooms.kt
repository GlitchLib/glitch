package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.ChatRoom

data class ChatRooms(
        override val data: List<ChatRoom>,
        @SerializedName("_total")
        override val size: Int
) : OrdinalList<ChatRoom>
