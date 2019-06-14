package io.glitchlib

import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.pubsub.GlitchPubSub

val GlitchClient.pubSub: GlitchPubSub
    get() = (this as GlitchClientImpl).let {
        it.use() ?: GlitchPubSub(this).apply {
            it.registerService(this)
        }
    }