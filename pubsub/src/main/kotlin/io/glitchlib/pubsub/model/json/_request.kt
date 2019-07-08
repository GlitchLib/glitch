package io.glitchlib.pubsub.model.json

import com.google.gson.JsonObject
import com.google.gson.annotations.JsonAdapter
import io.glitchlib.pubsub.model.MessageType
import io.glitchlib.pubsub.model.adapters.MessageTypeAdapter

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class TopicRequest(
        @JsonAdapter(MessageTypeAdapter::class)
        override val type: MessageType,
        val nonce: String,
        val data: JsonObject
) : DataType

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class SingleRequest(
        @JsonAdapter(MessageTypeAdapter::class)
        override val type: MessageType
) : DataType