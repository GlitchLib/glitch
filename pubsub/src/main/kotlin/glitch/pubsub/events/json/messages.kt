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
data class Following(val username: String, val displayName: String, val userId: Long)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Commerce(
        @SerializedName("user_name")
        val username: String,
        val displayName: String,
        val channelName: String,
        val userId: Long,
        val channelId: Long,
        @JsonAdapter(InstantConverter::class)
        val time: Instant,
        val itemImageUrl: String,
        val itemDescription: String,
        val isSupportsChannel: Boolean,
        val purchaseMessage: OrdinalMessage
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class OrdinalMessage(
        val emotes: List<Emote>,
        val message: String
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Emote(
        val end: Int,
        val id: Int,
        val start: Int
)