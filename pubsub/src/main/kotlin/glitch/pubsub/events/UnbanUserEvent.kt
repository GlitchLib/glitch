package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.Unban

class UnbanUserEvent(client: GlitchPubSub, topic: Topic, data: Unban) : AbstractMessageEvent<Unban>(client, topic, data), IEvent<GlitchPubSub> {

}
