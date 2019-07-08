package io.glitchlib.api.model

import com.google.gson.annotations.JsonAdapter
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
data class Stream(
        override val id: Long,
        val userId: Long,
        val userName: String,
        val gameId: Long,
        val communityIds: Collection<String>,
        @SerializedName("type")
        @JsonAdapter(StreamTypeSerializer::class)
        val isLive: Boolean,
        val title: String,
        val viewerCount: Int,
        val startedAt: Date,
        val language: Locale,
        val thumbnailUrl: String
) : IDObject<Long>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class StreamMarkerData(
        val userId: Long,
        @SerializedName("user_name")
        val username: String,
        val videos: StreamVod
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class StreamVod(
        @SerializedName("video_id")
        override val id: Long,
        val markers: Collection<Marker>
) : IDObject<Long> {
    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    data class Marker(
            override val id: String,
            override val createdAt: Date,
            val description: String,
            val positionSeconds: Long,
            @SerializedName("URL")
            val url: String
    ) : IDObject<String>, CreatedAt
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class MarkerCreate(
        override val id: Long,
        override val createdAt: Date,
        val description: String,
        val positionSeconds: Long
) : IDObject<Long>, CreatedAt