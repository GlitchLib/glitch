package io.glitchlib

import io.glitchlib.api.GlitchApi

val GlitchClient.api: GlitchApi
    get() = GlitchApi(this)