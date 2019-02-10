package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.ModeratorActivation

class SubscribersOnlyEvent(client: GlitchPubSub, topic: Topic, data: ModeratorActivation) : AbstractMessageEvent<ModeratorActivation>(client, topic, data), IEvent<GlitchPubSub> {

}
