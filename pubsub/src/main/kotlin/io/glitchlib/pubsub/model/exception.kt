package io.glitchlib.pubsub.model

import io.glitchlib.GlitchException

typealias PubSubException = GlitchException

class TopicException(message: String) : PubSubException(message)

class UnknownTopicException(message: String) : PubSubException(message)