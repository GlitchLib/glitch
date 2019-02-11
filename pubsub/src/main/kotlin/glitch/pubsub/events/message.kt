package glitch.pubsub.events

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.events.json.BitsMessage
import glitch.pubsub.events.json.Commerce
import glitch.pubsub.events.json.Following

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UnknownMessageEvent(
        override val client: GlitchPubSub,
        val rawTopic: String,
        val message: JsonObject
) : IEvent<GlitchPubSub>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class FollowEvent(
        override val client: GlitchPubSub,
        override val topic: Topic,
        override val message: Following
) : PubSubMessageEvent<Following>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class BitsEvent(
        override val client: GlitchPubSub,
        override val topic: Topic,
        override val message: BitsMessage
) : PubSubMessageEvent<BitsMessage>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class CommerceEvent(
        override val client: GlitchPubSub,
        override val topic: Topic,
        override val message: Commerce
) : PubSubMessageEvent<Commerce>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelExtensionBroadcastEvent(
        override val client: GlitchPubSub,
        override val topic: Topic,
        override val message: JsonArray
) : PubSubMessageEvent<JsonArray>