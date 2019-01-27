package glitch

import glitch.auth.GlitchScope

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Config(
        val clientId: String,
        val clientSecret: String,
        val userAgent: String,
        val defaultScopes: Set<GlitchScope>,
        val redirectUri: String?
)
