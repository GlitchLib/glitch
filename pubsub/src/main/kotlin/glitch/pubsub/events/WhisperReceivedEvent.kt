package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.WhisperMessage

class WhisperReceivedEvent(client: GlitchPubSub, topic: Topic, data: WhisperMessage) : AbstractMessageEvent<WhisperMessage>(client, topic, data), IEvent<GlitchPubSub> {

}
