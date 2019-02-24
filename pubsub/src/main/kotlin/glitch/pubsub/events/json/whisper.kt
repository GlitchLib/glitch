package glitch.pubsub.events.json

import com.fatboyindustrial.gsonjavatime.InstantConverter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import glitch.api.objects.enums.UserType
import glitch.api.objects.json.Badge
import glitch.api.objects.json.interfaces.IDObject
import java.awt.Color
import java.time.Instant

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class WhisperThread(
        val isArchived: Boolean,
        val isMuted: Boolean,
        @JsonAdapter(InstantConverter::class)
        val whitelistedUntil: Instant
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class WhisperMessage(
        override val id: Long,
        @SerializedName("body")
        val message: String,
        @SerializedName("sent_ts")
        @JsonAdapter(InstantConverter::class)
        val createdAt: Instant,
        @SerializedName("from_id")
        val senderId: Long,
        val tags: Tags,
        val recipient: Recipient
) : IDObject<Long> {

    data class Tags(
            val login: String,
            val displayName: String,
            val color: Color?,
            val userType: UserType,
            val emotes: Set<Emote>,
            val badges: Set<Badge>
    )

    data class Recipient(
            override val id: Long,
            val username: String,
            val displayName: String,
            val color: Color?,
            val userType: UserType,
            val badges: Set<Badge>,
            val profileImage: String
    ) : IDObject<Long>
}