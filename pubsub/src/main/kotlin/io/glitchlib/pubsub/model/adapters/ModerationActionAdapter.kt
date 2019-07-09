package io.glitchlib.pubsub.model.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.glitchlib.pubsub.model.json.ModerationData
import java.io.IOException

class ModerationActionAdapter : TypeAdapter<ModerationData.Action>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: ModerationData.Action) {
        out.value(value.name.toLowerCase())
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): ModerationData.Action {
        return ModerationData.Action.valueOf(`in`.nextString().toUpperCase())
    }
}
