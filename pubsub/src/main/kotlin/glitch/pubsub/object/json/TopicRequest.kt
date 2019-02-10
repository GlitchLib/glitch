package glitch.pubsub.`object`.json

import com.google.gson.JsonObject
import glitch.pubsub.`object`.enums.MessageType

class TopicRequest(type: MessageType, val nonce: String, val data: JsonObject) : SingleRequestType(type) {

}
