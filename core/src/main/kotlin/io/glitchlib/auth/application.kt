package io.glitchlib.auth

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.CreatedAndUpdatedAt
import java.util.Date

data class AppAccessToken(
    val accessToken: String,
    val expiresIn: Long,
    val tokenType: String,
    val scope: Collection<Scope>
)

data class AppValidate(
    val clientId: String,
    val scopes: Collection<Scope>
)

data class AppCredential internal constructor(
    override val createdAt: Date,
    override val updatedAt: Date,
    val accessToken: String,
    val expiresIn: Long,
    val scopes: Collection<Scope>,
    val clientId: String
) : CreatedAndUpdatedAt {
    constructor(token: AppAccessToken, kraken: AppKraken) : this(
        kraken.authorizaton.createdAt,
        kraken.authorizaton.updatedAt,
        token.accessToken,
        token.expiresIn,
        token.scope,
        kraken.clientId
    )
}

data class AppKraken(
    @get:SerializedName("valid")
    val isValid: Boolean,
    val authorizaton: Kraken.Authorization,
    val clientId: String,
    val expiresIn: Long
)