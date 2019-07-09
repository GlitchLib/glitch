package io.glitchlib.pubsub

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.glitchlib.pubsub.model.MessageType
import io.glitchlib.pubsub.model.json.TopicRequest

internal fun Topic.listen() = JsonObject().apply {
    add("topics", JsonArray().apply { add(rawType) })
    if (credential != null) addProperty("auth_token", credential!!.accessToken)
}.let {
    TopicRequest(MessageType.LISTEN, this.code.toString(), it)
}

internal fun Topic.unlisten() = JsonObject().apply {
    add("topics", JsonArray().apply { add(rawType) })
    if (credential != null) addProperty("auth_token", credential!!.accessToken)
}.let {
    TopicRequest(MessageType.UNLISTEN, this.code.toString(), it)
}

