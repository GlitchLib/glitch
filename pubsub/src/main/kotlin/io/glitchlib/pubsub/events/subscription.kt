package io.glitchlib.pubsub.events

import io.glitchlib.GlitchClient
import io.glitchlib.pubsub.Topic
import io.glitchlib.pubsub.model.json.GiftSubscriptionMessage
import io.glitchlib.pubsub.model.json.SubscriptionMessage

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class SubscriptionEvent(
    override val client: GlitchClient,
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
    override val client: GlitchClient,
    override val topic: Topic,
    override val message: GiftSubscriptionMessage
) : PubSubMessageEvent<GiftSubscriptionMessage>