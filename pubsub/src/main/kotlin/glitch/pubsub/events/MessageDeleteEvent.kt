package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.MessageDelete

class MessageDeleteEvent(client: GlitchPubSub, topic: Topic, data: MessageDelete) : AbstractMessageEvent<MessageDelete>(client, topic, data), IEvent<GlitchPubSub>
