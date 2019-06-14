package io.glitchlib.pubsub.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.Badge
import io.glitchlib.model.IDObject
import io.glitchlib.model.UserType
import java.awt.Color
import java.util.Date

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class WhisperThread(
    val isArchived: Boolean,
    val isMuted: Boolean,
    val whitelistedUntil: Date
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
    val createdAt: Date,
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