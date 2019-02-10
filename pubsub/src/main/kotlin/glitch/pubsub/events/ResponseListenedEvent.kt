package glitch.pubsub.events

import glitch.api.ws.events.AbstractEvent
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.exceptions.PubSubException
import java.util.*

class ResponseListenedEvent(
        client: GlitchPubSub,
        val nonce: UUID,
        val error: PubSubException
) : AbstractEvent<GlitchPubSub>(client), IEvent<GlitchPubSub> {

}
