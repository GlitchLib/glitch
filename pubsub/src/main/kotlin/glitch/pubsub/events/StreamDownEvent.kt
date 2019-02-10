package glitch.pubsub.events

import glitch.api.ws.events.AbstractEvent
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import java.time.Instant

class StreamDownEvent(client: GlitchPubSub, val topic: Topic, val serverTime: Instant) : AbstractEvent<GlitchPubSub>(client), IEvent<GlitchPubSub> {
}
