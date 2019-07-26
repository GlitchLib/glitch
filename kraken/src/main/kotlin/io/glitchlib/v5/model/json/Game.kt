package io.glitchlib.v5.model.json

import io.glitchlib.model.IDObject

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class Game(
    override val id: Long,
    val box: Image,
    val gigantbombId: Long,
    val logo: Image,
    val name: String,
    val popularity: Long
) : IDObject<Long>
