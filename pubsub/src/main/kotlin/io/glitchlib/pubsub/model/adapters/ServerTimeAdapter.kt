package io.glitchlib.pubsub.model.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.Instant
import java.util.*

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class ServerTimeAdapter : JsonSerializer<Date>, JsonDeserializer<Date> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        val secdons = json.asString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val s = secdons[0].toLong()
        val ns = secdons[1].toLong() * 100
        return Date.from(Instant.ofEpochSecond(s, ns))
    }

    override fun serialize(src: Date, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toInstant().nano)
    }
}
