package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.GiftSubscriptionMessage

class SubGiftEvent(client: GlitchPubSub, topic: Topic, data: GiftSubscriptionMessage) : AbstractMessageEvent<GiftSubscriptionMessage>(client, topic, data), IEvent<GlitchPubSub> {

}
