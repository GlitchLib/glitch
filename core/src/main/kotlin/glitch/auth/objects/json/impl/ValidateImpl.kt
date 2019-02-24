package glitch.auth.objects.json.impl

import glitch.auth.GlitchScope
import glitch.auth.objects.json.Validate

data class ValidateImpl(
        override val clientId: String,
        override val login: String,
        override val scopes: Set<GlitchScope>,
        override val userId: Long
) : Validate
