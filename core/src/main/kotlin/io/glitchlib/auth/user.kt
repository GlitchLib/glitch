package io.glitchlib.auth

import com.google.gson.annotations.SerializedName
import io.glitchlib.GlitchException
import io.glitchlib.internal.auth.TokenImpl
import io.glitchlib.model.Badge
import io.glitchlib.model.CreatedAndUpdatedAt
import io.glitchlib.model.IDObject
import java.util.Date


data class AccessToken(
    override val accessToken: String,
    override val refreshToken: String,
    @SerializedName("scope")
    val scopes: Set<Scope>,
    val expiresIn: Long,
    val tokenType: String
) : Token

data class Validate(
    @SerializedName("user_id")
    override val id: Long,
    val clientId: String,
    val login: String,
    val scopes: Set<Scope>
) : IDObject<Long>

data class GlobalUserState(
    override val id: Long,
    val login: String,
    val displayName: String,
    val color: String,
    val isVerifiedBot: Boolean,
    val isKnownBot: Boolean,
    val badges: Collection<Badge>
) : IDObject<Long>

data class Credential internal constructor(
    override val createdAt: Date,
    override val updatedAt: Date,
    override val accessToken: String,
    override val refreshToken: String,
    override val id: Long,
    val clientId: String,
    val username: String,
    val chat: Chat,
    val scopes: Collection<Scope>
) : CreatedAndUpdatedAt, Token, IDObject<Long> {

    internal constructor(token: Token, validate: Validate, gus: GlobalUserState, kraken: Kraken) : this(
        kraken.authorizaton.createdAt,
        kraken.authorizaton.updatedAt,
        token.accessToken,
        token.refreshToken,
        validate.id,
        validate.clientId,
        validate.login,
        Chat(
            gus.displayName,
            gus.color,
            gus.isVerifiedBot,
            gus.isKnownBot,
            gus.badges
        ),
        validate.scopes
    )

    internal constructor(token: AccessToken, validate: Validate, gus: GlobalUserState, kraken: Kraken) : this(
        kraken.authorizaton.createdAt,
        kraken.authorizaton.updatedAt,
        token.accessToken,
        token.refreshToken,
        validate.id,
        validate.clientId,
        validate.login,
        Chat(
            gus.displayName,
            gus.color,
            gus.isVerifiedBot,
            gus.isKnownBot,
            gus.badges
        ),
        token.scopes
    )

    data class Chat(
        val displayName: String,
        val color: String,
        val isVerifiedBot: Boolean,
        val isKnownBot: Boolean,
        val badges: Collection<Badge>
    )
}

interface Token {
    val accessToken: String
    val refreshToken: String

    companion object {
        @JvmStatic
        fun of(accessToken: String, refreshToken: String): Token = TokenImpl(accessToken, refreshToken)

        operator fun invoke(accessToken: String, refreshToken: String): Token = TokenImpl(accessToken, refreshToken)
    }
}

data class Kraken(
    @SerializedName("valid")
    val isValid: Boolean,
    val authorizaton: Authorization,
    val clientId: String,
    val userName: String
) {

    data class Authorization(
        val scopes: Collection<Scope>,
        override val createdAt: Date,
        override val updatedAt: Date
    ) : CreatedAndUpdatedAt
}

class ScopeIsMissingException(val scope: Scope) : GlitchException("Required scope: ${scope.value}")