package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.Ban

class BanUserEvent(client: GlitchPubSub, topic: Topic, data: Ban) : AbstractMessageEvent<Ban>(client, topic, data), IEvent<GlitchPubSub>
