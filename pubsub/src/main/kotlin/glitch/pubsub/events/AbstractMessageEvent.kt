package glitch.pubsub.events

import glitch.api.ws.events.AbstractEvent
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic

abstract class AbstractMessageEvent<T> protected constructor(client: GlitchPubSub, val topic: Topic, val data: T) : AbstractEvent<GlitchPubSub>(client), IEvent<GlitchPubSub>
