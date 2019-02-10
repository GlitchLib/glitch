package glitch.pubsub.`object`.json

import com.google.gson.annotations.JsonAdapter
import glitch.pubsub.`object`.adapters.VideoPlaybackTypeAdapter
import java.time.Instant

class VideoPlayback(
        @JsonAdapter(VideoPlaybackTypeAdapter::class)
        val type: Type, val serverTime: Instant) {

    enum class Type {
        STREAM_UP,
        STREAM_DOWN,
        VIEW_COUNT
    }
}
