package glitch.auth.objects.json

import glitch.auth.GlitchScope

interface Validate {
    val clientId: String

    val login: String

    val scopes: Set<GlitchScope>

    val userId: Long
}
