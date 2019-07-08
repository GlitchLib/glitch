package io.glitchlib.api.model

import io.glitchlib.model.IDObject

data class Game(
        override val id: Long,
        val name: String,
        val boxArtUrl: String
) : IDObject<Long>
