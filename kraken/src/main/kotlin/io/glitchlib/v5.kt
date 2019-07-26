package io.glitchlib

import io.glitchlib.v5.GlitchKraken

val GlitchClient.v5: GlitchKraken
    get() = GlitchKraken(this)