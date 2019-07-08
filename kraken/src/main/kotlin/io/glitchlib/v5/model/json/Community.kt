package io.glitchlib.v5.model.json

import io.glitchlib.model.IDObject
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Community(
        override val id: UUID,
        val ownerId: Long,
        val name: String,
        val displayName: String,
        val avatarImageUrl: String,
        val coverImageUrl: String,
        val description: String,
        val descriptionHtml: String,
        val rules: String,
        val rulesHtml: String,
        val language: Locale,
        val summary: String
) : IDObject<UUID>
