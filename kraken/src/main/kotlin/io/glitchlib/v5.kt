package io.glitchlib

import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.v5.GlitchKraken

val GlitchClient.v5: GlitchKraken
    get() = (this as GlitchClientImpl).let {
        it.use() ?: GlitchKraken(this).apply {
            it.registerService(this)
        }
    }