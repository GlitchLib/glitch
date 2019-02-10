package glitch.pubsub.`object`.json

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class CommerceMessage(
        @SerializedName("user_name")
        val username: String, val displayName: String,
        val channelName: String, val userId: Long,
        val channelId: Long, val time: Instant,
        val itemImageUrl: String, val itemDescription: String,
        val isSupportsChannel: Boolean, val purchaseMessage: String
)
