package io.glitchlib.pubsub.events

import com.google.gson.JsonObject
import io.glitchlib.GlitchClient
import io.glitchlib.model.IEvent
import io.glitchlib.pubsub.Topic

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class PubSubEvent(
        override val client: GlitchClient,
        val data: JsonObject
) : IEvent

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ReconnectRequiredEvent(
        override val client: GlitchClient
) : IEvent

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class SuccessfulResponseEvent(
        override val client: GlitchClient,
        val topic: Topic
) : IEvent

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ErrorResponseEvent(
        override val client: GlitchClient,
        val topic: Topic? = null,
        val cause: Throwable
) : IEvent
