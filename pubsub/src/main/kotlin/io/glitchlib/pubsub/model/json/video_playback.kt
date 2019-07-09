package io.glitchlib.pubsub.model.json

import com.google.gson.annotations.JsonAdapter
import io.glitchlib.pubsub.model.adapters.ServerTimeAdapter
import io.glitchlib.pubsub.model.adapters.VideoPlaybackTypeAdapter
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class VideoPlayback(
        @JsonAdapter(VideoPlaybackTypeAdapter::class)
        val type: Type,
        @JsonAdapter(ServerTimeAdapter::class)
        val serverTime: Date
) {

    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    enum class Type {
        STREAM_UP,
        STREAM_DOWN,
        VIEW_COUNT
    }
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class StreamUp(
        @JsonAdapter(ServerTimeAdapter::class)
        val serverTime: Date,
        val delay: Int
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ViewCount(
        @JsonAdapter(ServerTimeAdapter::class)
        val serverTime: Date,
        val viewers: Long
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class StreamDown(
        @JsonAdapter(ServerTimeAdapter::class)
        val serverTime: Date
)