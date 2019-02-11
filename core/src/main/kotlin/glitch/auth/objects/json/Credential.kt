package glitch.auth.objects.json

import glitch.auth.GlitchScope
import glitch.auth.UserCredential
import java.time.Instant
import java.time.temporal.ChronoUnit

data class Credential(
        override val accessToken: String,
        override val refreshToken: String,
        override val expiredAt: Instant,
        override val scopes: Set<GlitchScope>,
        override val clientId: String,
        override val login: String,
        override val userId: Long
) : AccessToken, Validate {
    constructor(token: AccessToken, validate: Validate) : this(
            token.accessToken,
            token.refreshToken,
            token.expiredAt,
            validate.scopes,
            validate.clientId,
            validate.login,
            validate.userId
    )

    constructor(token: UserCredential, validate: Validate) : this(
            token.accessToken,
            token.refreshToken,
            Instant.now().plus(60, ChronoUnit.DAYS),
            validate.scopes,
            validate.clientId,
            validate.login,
            validate.userId
    )
}
