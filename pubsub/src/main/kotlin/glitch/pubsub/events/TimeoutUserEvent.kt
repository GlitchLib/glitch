package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.Timeout

class TimeoutUserEvent(client: GlitchPubSub, topic: Topic, data: Timeout) : AbstractMessageEvent<Timeout>(client, topic, data), IEvent<GlitchPubSub> {

}
