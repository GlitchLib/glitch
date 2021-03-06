package glitch.pubsub.events.json

import com.fatboyindustrial.gsonjavatime.InstantConverter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.time.Instant

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class BitsMessage(
        val `data`: Data,
        val messageId: String,
        val messageType: String,
        val version: String,
        val isAnonymous: Boolean = data.userId != null && data.username != null
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Data(
        val badgeEntitlement: BadgeEntitlement?,
        val bitsUsed: Int,
        val channelId: Long,
        val channelName: String,
        @SerializedName("chat_message")
        val message: String,
        val context: String,
        @JsonAdapter(InstantConverter::class)
        val time: Instant,
        val totalBitsUsed: Int,
        val userId: Long?,
        @SerializedName("user_name")
        val username: String?
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class BadgeEntitlement(
        val newVersion: Int,
        val previousVersion: Int
)