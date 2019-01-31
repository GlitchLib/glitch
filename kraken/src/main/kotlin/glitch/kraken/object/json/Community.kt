package glitch.kraken.`object`.json

import glitch.api.objects.json.interfaces.IDObject
import java.util.Locale
import java.util.UUID

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
