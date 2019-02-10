package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.CommerceMessage

class CommerceEvent(client: GlitchPubSub, topic: Topic, data: CommerceMessage) : AbstractMessageEvent<CommerceMessage>(client, topic, data), IEvent<GlitchPubSub>
