package io.glitchlib.pubsub.model

import io.glitchlib.GlitchClient
import io.glitchlib.model.GlitchObject

data class EventObject(
    override val client: GlitchClient
) : GlitchObject