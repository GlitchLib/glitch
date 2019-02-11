package glitch.pubsub.events

import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.events.json.StreamDown
import glitch.pubsub.events.json.StreamUp
import glitch.pubsub.events.json.ViewCount

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class StreamUpEvent(
        override val client: GlitchPubSub,
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
        override val client: GlitchPubSub,
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
        override val client: GlitchPubSub,
        override val topic: Topic,
        override val message: ViewCount
) : PubSubMessageEvent<ViewCount>