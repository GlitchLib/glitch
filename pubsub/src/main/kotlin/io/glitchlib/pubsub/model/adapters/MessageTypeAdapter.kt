package io.glitchlib.pubsub.model.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.glitchlib.pubsub.model.MessageType
import java.io.IOException

class MessageTypeAdapter : TypeAdapter<MessageType>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: MessageType) {
        out.value(value.name)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): MessageType {
        return MessageType.valueOf(`in`.nextString())
    }
}
