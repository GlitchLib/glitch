package io.glitchlib.pubsub.model.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.glitchlib.pubsub.model.json.VideoPlayback
import java.io.IOException

class VideoPlaybackTypeAdapter : TypeAdapter<VideoPlayback.Type>() {

    /**
     * {@inheritDoc}
     */
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: VideoPlayback.Type) {
        out.value(value.name.toLowerCase())
    }

    /**
     * {@inheritDoc}
     */
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): VideoPlayback.Type {
        val type = `in`.nextString().replace("-", "_")
        return if (type.equals("viewcount", ignoreCase = true)) {
            VideoPlayback.Type.VIEW_COUNT
        } else {
            VideoPlayback.Type.valueOf(type.toUpperCase())
        }
    }
}
