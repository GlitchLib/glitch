package io.glitchlib.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class ImplementationSerializerAdapter<T : Any> : JsonSerializer<T>, JsonDeserializer<T> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): T {
        val realType = getTypedArgument(type)
        if (realType.isInterface && realType.isAnnotationPresent(SerializeTo::class.java)) {
            val serializedClass = realType.getAnnotation(SerializeTo::class.java).value

            if (serializedClass.java.isAssignableFrom(realType)) {
                return context.deserialize(json, serializedClass.java)
            }
        }
        return context.deserialize(json, type)
    }

    override fun serialize(src: T, type: Type, context: JsonSerializationContext): JsonElement {
        return context.serialize(src, type)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getTypedArgument(type: Type): Class<T> = type as Class<T>
}

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class SerializeTo(val value: KClass<*>)

class SubscriptionTypeAdapter : TypeAdapter<SubscriptionType>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: SubscriptionType) {
        out.value(if (value.value == "") null else value.value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): SubscriptionType {
        return SubscriptionType.from(`in`.nextString())
    }
}

class UserTypeAdapter : TypeAdapter<UserType>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: UserType) {
        out.value(value.name.toLowerCase())
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): UserType {
        return UserType.from(`in`.nextString())
    }
}

class VideoTypeAdapter : TypeAdapter<VideoType>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: VideoType) {
        out.value(value.name.toLowerCase())
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): VideoType {
        return VideoType.valueOf(`in`.nextString().toUpperCase())
    }
}

class VideoViewTypeAdapter : TypeAdapter<ViewType>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: ViewType) {
        out.value(value.name.toLowerCase())
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): ViewType {
        return ViewType.valueOf(`in`.nextString().toUpperCase())
    }
}
