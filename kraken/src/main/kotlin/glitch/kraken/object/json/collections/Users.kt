package glitch.kraken.`object`.json.collections

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.OrdinalList
import glitch.kraken.`object`.json.User

class Users(
        override val data: List<User>,
        @SerializedName("_total")
        override val size: Int
) : OrdinalList<User>