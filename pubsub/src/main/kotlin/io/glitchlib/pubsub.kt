package io.glitchlib

import io.glitchlib.pubsub.GlitchPubSub

val GlitchClient.pubSub: GlitchPubSub
    get() = GlitchPubSub(this)