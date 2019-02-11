package glitch.helix.`object`.json

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.IDObject
import glitch.helix.`object`.adapters.StreamTypeSerializer
import java.time.Instant
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Stream(
        override val id: Long,
        val userId: Long,
        val userName: String,
        val gameId: Long,
        val communityIds: Array<String>,
        @SerializedName("type")
        @JsonAdapter(StreamTypeSerializer::class)
        val isLive: Boolean,
        val title: String,
        val viewerCount: Int,
        val startedAt: Instant,
        val language: Locale,
        val thumbnailUrl: String
) : IDObject<Long>