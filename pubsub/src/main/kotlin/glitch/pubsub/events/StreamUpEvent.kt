package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.StreamUp

class StreamUpEvent(client: GlitchPubSub, topic: Topic, data: StreamUp) : AbstractMessageEvent<StreamUp>(client, topic, data), IEvent<GlitchPubSub> {
}
