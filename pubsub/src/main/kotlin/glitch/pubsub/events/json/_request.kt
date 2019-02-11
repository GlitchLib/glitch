package glitch.pubsub.events.json

import com.google.gson.JsonObject
import glitch.pubsub.`object`.enums.MessageType

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class TopicRequest(
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
        override val type: MessageType
) : DataType