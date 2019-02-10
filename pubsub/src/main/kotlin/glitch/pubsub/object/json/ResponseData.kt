package glitch.pubsub.`object`.json

import com.google.gson.JsonObject
import glitch.pubsub.Topic

class ResponseData(val topic: Topic, val message: JsonObject) {
}
