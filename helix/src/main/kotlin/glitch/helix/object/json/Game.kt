package glitch.helix.`object`.json

import glitch.api.objects.json.interfaces.IDObject

data class Game(
        override val id: Long,
        val name: String,
        val boxArtUrl: String
) : IDObject<Long>
