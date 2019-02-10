package glitch.pubsub.events

import com.google.gson.JsonElement
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic

class RawMessageEvent(client: GlitchPubSub, topic: Topic, jsonElement: JsonElement) : AbstractMessageEvent<JsonElement>(client, topic, jsonElement), IEvent<GlitchPubSub> {
}
