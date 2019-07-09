package io.glitchlib.v5.model

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.glitchlib.v5.model.json.Game
import io.glitchlib.v5.model.json.Video
import java.io.IOException
import java.lang.reflect.Type

class GameNameSerializer : JsonSerializer<Game> {
    override fun serialize(src: Game, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.name)
    }
}

class VideoIdAdapter : TypeAdapter<Long>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Long) {
        out.value("v$value")
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Long {
        return `in`.nextString().substring(1).toLong()
    }
}

class VideoStatusAdapter : TypeAdapter<Video.Status>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Video.Status) {
        out.value(value.name.toLowerCase())
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Video.Status {
        return Video.Status.valueOf(`in`.nextString().toUpperCase())
    }
}