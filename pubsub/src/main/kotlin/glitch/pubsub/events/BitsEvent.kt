package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.BitsMessage

class BitsEvent(client: GlitchPubSub, topic: Topic, data: BitsMessage) : AbstractMessageEvent<BitsMessage>(client, topic, data), IEvent<GlitchPubSub>
