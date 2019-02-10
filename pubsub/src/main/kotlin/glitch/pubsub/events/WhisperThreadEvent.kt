package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.WhisperThread

class WhisperThreadEvent(client: GlitchPubSub, topic: Topic, data: WhisperThread) : AbstractMessageEvent<WhisperThread>(client, topic, data), IEvent<GlitchPubSub>
