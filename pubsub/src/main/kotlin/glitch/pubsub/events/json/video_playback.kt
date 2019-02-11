package glitch.pubsub.events.json

import com.google.gson.annotations.JsonAdapter
import glitch.pubsub.`object`.adapters.VideoPlaybackTypeAdapter
import java.time.Instant

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class VideoPlayback(
        @JsonAdapter(VideoPlaybackTypeAdapter::class)
        val type: Type,
        val serverTime: Instant
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
        val serverTime: Instant,
        val delay: Int
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class ViewCount(val serverTime: Instant, val viewers: Long)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class StreamDown(
        val serverTime: Instant
)