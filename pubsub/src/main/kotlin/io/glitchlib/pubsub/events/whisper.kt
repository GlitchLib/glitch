package io.glitchlib.pubsub.events

import io.glitchlib.GlitchClient
import io.glitchlib.pubsub.Topic
import io.glitchlib.pubsub.model.json.WhisperMessage
import io.glitchlib.pubsub.model.json.WhisperThread

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class WhisperThreadEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: WhisperThread
) : PubSubMessageEvent<WhisperThread>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class WhisperReceivedEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: WhisperMessage
) : PubSubMessageEvent<WhisperMessage>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class WhisperSentEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: WhisperMessage
) : PubSubMessageEvent<WhisperMessage>