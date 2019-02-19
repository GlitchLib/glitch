package glitch.auth.objects.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.Creation
import glitch.api.objects.json.interfaces.Updated
import glitch.auth.GlitchScope
import java.time.Instant

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Kraken(
    @SerializedName("valid")
    val isValid: Boolean,
    val authorization: Authorization,
    @SerializedName("user_name")
    val username: String,
    val userId: Long,
    val clientId: String
)

data class Authorization(
        @SerializedName("scopes")
        val scope: Set<GlitchScope>,
        override val createdAt: Instant,
        override val updatedAt: Instant
): Creation, Updated