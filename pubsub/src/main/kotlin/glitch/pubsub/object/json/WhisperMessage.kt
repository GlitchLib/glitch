package glitch.pubsub.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.enums.UserType
import glitch.api.objects.json.Badge
import glitch.api.objects.json.interfaces.IDObject
import glitch.pubsub.`object`.json.message.Emote
import java.awt.Color
import java.time.Instant

class WhisperMessage(
        override val id: Long,
        @SerializedName("body")
        val message: String,
        @SerializedName("sent_ts")
        val createdAt: Instant,
        @SerializedName("from_id")
        val senderId: Long, val tags: Tags,
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
