package io.glitchlib

import io.glitchlib.webhook.GlitchWebhook
import io.glitchlib.internal.GlitchClientImpl

val GlitchClient.webhook: GlitchWebhook
    get() = (this as GlitchClientImpl).let {
        it.use() ?: GlitchWebhook(it).apply {
            it.registerService(this)
        }
    }