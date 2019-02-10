package glitch.pubsub.`object`.json

import com.google.gson.annotations.JsonAdapter
import glitch.pubsub.`object`.adapters.ExceptionAdapter
import glitch.pubsub.`object`.adapters.MessageTypeAdapter
import glitch.pubsub.`object`.enums.MessageType
import glitch.pubsub.exceptions.PubSubException

class Response(
        @JsonAdapter(MessageTypeAdapter::class)
        val type: MessageType,
        val nonce: String?,
        @JsonAdapter(ExceptionAdapter::class)
        val error: PubSubException?,
        val data: ResponseData?
) {
}
