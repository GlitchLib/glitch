package glitch.pubsub.events

import com.google.gson.JsonElement
import glitch.api.ws.events.AbstractEvent
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub

class UnknownResponseEvent(client: GlitchPubSub, val data: JsonElement) : AbstractEvent<GlitchPubSub>(client), IEvent<GlitchPubSub> {
}
