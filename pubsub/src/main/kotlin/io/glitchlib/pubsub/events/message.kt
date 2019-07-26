package io.glitchlib.pubsub.events

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.glitchlib.GlitchClient
import io.glitchlib.model.IEvent
import io.glitchlib.pubsub.Topic
import io.glitchlib.pubsub.model.json.BitsMessage
import io.glitchlib.pubsub.model.json.Commerce
import io.glitchlib.pubsub.model.json.Following

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UnknownMessageEvent(
    override val client: GlitchClient,
    val rawTopic: String,
    val message: JsonObject
) : IEvent

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class FollowEvent(
    override val client: GlitchClient,
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
    override val client: GlitchClient,
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
    override val client: GlitchClient,
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
    override val client: GlitchClient,
    override val topic: Topic,
    override val message: JsonArray
) : PubSubMessageEvent<JsonArray>