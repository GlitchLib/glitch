package io.glitchlib.pubsub.events

import io.glitchlib.model.IEvent
import io.glitchlib.pubsub.Topic

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface PubSubMessageEvent<T> : IEvent {
    val topic: Topic
    val message: T
}