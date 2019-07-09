package io.glitchlib

import io.glitchlib.api.GlitchApi
import io.glitchlib.internal.GlitchClientImpl

val GlitchClient.api: GlitchApi
    get() = (this as GlitchClientImpl).let {
        it.use() ?: GlitchApi(this).apply {
            it.registerService(this)
        }
    }