package glitch.pubsub.events

import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.`object`.json.Moderator

class ClearChatEvent(client: GlitchPubSub, topic: Topic, data: Moderator) : AbstractMessageEvent<Moderator>(client, topic, data), IEvent<GlitchPubSub>
