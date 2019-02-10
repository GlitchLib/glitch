package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.ViewCount

class ViewCountEvent(client: GlitchPubSub, topic: Topic, data: ViewCount) : AbstractMessageEvent<ViewCount>(client, topic, data), IEvent<GlitchPubSub> {

}
