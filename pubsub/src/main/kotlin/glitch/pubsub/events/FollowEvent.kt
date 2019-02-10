package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.Following

class FollowEvent(client: GlitchPubSub, topic: Topic, message: Following) : AbstractMessageEvent<Following>(client, topic, message), IEvent<GlitchPubSub> {

}
