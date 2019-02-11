package glitch.pubsub.events

import glitch.pubsub.GlitchPubSub
import glitch.pubsub.Topic
import glitch.pubsub.events.json.GiftSubscriptionMessage
import glitch.pubsub.events.json.SubscriptionMessage

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class SubscriptionEvent(
        override val client: GlitchPubSub,
        override val topic: Topic,
        override val message: SubscriptionMessage
) : PubSubMessageEvent<SubscriptionMessage>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class SubGiftEvent(
        override val client: GlitchPubSub,
        override val topic: Topic,
        override val message: GiftSubscriptionMessage
) : PubSubMessageEvent<GiftSubscriptionMessage>