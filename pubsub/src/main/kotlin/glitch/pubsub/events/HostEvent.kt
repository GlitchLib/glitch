package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.Host

class HostEvent(client: GlitchPubSub, topic: Topic, data: Host) : AbstractMessageEvent<Host>(client, topic, data), IEvent<GlitchPubSub> {

}
