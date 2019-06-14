package io.glitchlib.pubsub.model


enum class MessageType {
    PING,
    PONG,
    RECONNECT,
    LISTEN,
    UNLISTEN,
    RESPONSE,
    MESSAGE
}

enum class SubscriptionContext {
    SUB,
    RESUB,
    SUBGIFT
}