package io.glitchlib

import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.tmi.MessageInterface

val GlitchClient.messageInterface: MessageInterface
    get() = (this as GlitchClientImpl).let {
        it.use() ?: MessageInterface(this).apply {
            it.registerService(this)
        }
    }