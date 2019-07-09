package io.glitchlib.v5.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.CreatedAt
import io.glitchlib.model.IDObject
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class FeatureStream(
        val image: String,
        val priority: Int,
        val isScheduled: Boolean,
        val isSponsored: Boolean,
        val stream: Stream,
        val text: String,
        val title: String
)

data class Stream(
        override val id: Long,
        val game: String,
        val viewers: Int,
        val videoHeight: Int,
        val averageFps: Int,
        val delay: Int,
        override val createdAt: Date,
        @SerializedName("is_playlist")
        val isPlaylist: Boolean,
        val preview: Image,
        val channel: Channel
) : IDObject<Long>, CreatedAt

data class StreamSummary(
        val channels: Int,
        val viewers: Int
)