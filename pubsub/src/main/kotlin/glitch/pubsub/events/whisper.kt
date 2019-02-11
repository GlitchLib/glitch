package glitch.pubsub.events

import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.events.json.WhisperMessage
import glitch.pubsub.events.json.WhisperThread

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class WhisperThreadEvent(
        override val client: GlitchPubSub,
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
        override val client: GlitchPubSub,
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
        override val client: GlitchPubSub,
        override val topic: Topic,
        override val message: WhisperMessage
) : PubSubMessageEvent<WhisperMessage>