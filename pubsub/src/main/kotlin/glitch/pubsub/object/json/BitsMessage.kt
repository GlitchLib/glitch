package glitch.pubsub.`object`.json

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class BitsMessage(
        @SerializedName("user_name")
        val username: String, val channelName: String, val userId: Long,
        val channelId: Long, val time: Instant,
        @SerializedName("chat_message")
        val message: String, val bitsUsed: Int,
        val totalBitsUsed: Int, val context: String
)
