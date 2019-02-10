package glitch.pubsub.events

import com.google.gson.JsonArray
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic

class ChannelExtensionBroadcastEvent(client: GlitchPubSub, topic: Topic, data: JsonArray) : AbstractMessageEvent<JsonArray>(client, topic, data), IEvent<GlitchPubSub>
