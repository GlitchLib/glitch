package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.SubscriptionMessage

class SubscriptionEvent(client: GlitchPubSub, topic: Topic, data: SubscriptionMessage) : AbstractMessageEvent<SubscriptionMessage>(client, topic, data), IEvent<GlitchPubSub> {

}
