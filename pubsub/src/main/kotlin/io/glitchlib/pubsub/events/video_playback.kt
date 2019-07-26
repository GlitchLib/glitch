package io.glitchlib.pubsub.events

import io.glitchlib.GlitchClient
import io.glitchlib.pubsub.Topic
import io.glitchlib.pubsub.model.json.StreamDown
import io.glitchlib.pubsub.model.json.StreamUp
import io.glitchlib.pubsub.model.json.ViewCount

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class StreamUpEvent(
    override val client: GlitchClient,
    override val topic: Topic,
    override val message: StreamUp
) : PubSubMessageEvent<StreamUp>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class StreamDownEvent(
    override val client: GlitchClient,
    override val topic: Topic,
    override val message: StreamDown
) : PubSubMessageEvent<StreamDown>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ViewCountEvent(
    override val client: GlitchClient,
    override val topic: Topic,
    override val message: ViewCount
) : PubSubMessageEvent<ViewCount>