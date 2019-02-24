package glitch.pubsub.events

import com.google.gson.JsonObject
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.exceptions.PubSubException

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class PubSubEvent(
        override val client: GlitchPubSub,
        val data: JsonObject
) : IEvent<GlitchPubSub>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ReconnectRequiredEvent(
        override val client: GlitchPubSub
) : IEvent<GlitchPubSub>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class SucessfulResponseEvent(
        override val client: GlitchPubSub,
        val topic: Topic
) : IEvent<GlitchPubSub>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ErrorResponseEvent(
        override val client: GlitchPubSub,
        val topic: Topic? = null,
        override val cause: Throwable
) : PubSubException(cause), IEvent<GlitchPubSub>
