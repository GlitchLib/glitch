package glitch.pubsub.events

import glitch.api.ws.events.AbstractEvent
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub

class ReconnectAlertEvent(client: GlitchPubSub) : AbstractEvent<GlitchPubSub>(client), IEvent<GlitchPubSub> {
}
