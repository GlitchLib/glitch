package io.glitchlib.internal.auth

import com.google.gson.JsonObject
import io.glitchlib.URL
import io.glitchlib.auth.AccessToken
import io.glitchlib.auth.AppAccessToken
import io.glitchlib.auth.AppCredential
import io.glitchlib.auth.AppKraken
import io.glitchlib.auth.AppValidate
import io.glitchlib.auth.Credential
import io.glitchlib.auth.IAuthorize
import io.glitchlib.auth.Kraken
import io.glitchlib.auth.Scope
import io.glitchlib.auth.Token
import io.glitchlib.auth.Validate
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.internal.http.body
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.internal.http.completed
import io.reactivex.Completable
import io.reactivex.Single

class AuthorizeImpl(
    override val client: GlitchClientImpl,
    override val storage: IAuthorize.IStorage
) : IAuthorize {

    override fun validate(credential: Credential): Single<Validate> = validate(credential.accessToken)

    override fun validate(token: Token): Single<Validate> = validate(token.accessToken)

    override fun validateApp(credential: AppCredential): Single<AppValidate> = validateApp(credential.accessToken)

    override fun krakenValidate(credential: Credential): Single<Kraken> = krakenValidate(credential.accessToken)

    override fun krakenValidate(token: Token): Single<Kraken> = krakenValidate(token.accessToken)

    override fun krakenValidateApp(credential: AppCredential): Single<AppKraken> =
        krakenValidateApp(credential.accessToken)

    override fun createAppToken(scope: Set<Scope>) =
        client.http.post<AppAccessToken>(URL.OAUTH.newBuilder().addPathSegment("token").build()) {
            val scopes = (client.settings.defaultScope + scope)
            addQueryParameters("client_id", client.settings.clientId)
            addQueryParameters("client_secret", client.settings.clientSecret)
            if (scopes.isNotEmpty()) {
                addQueryParameters("scope", scopes.joinToString("+"))
            }
            addQueryParameters("grant_type", "client_credentials")
        }.bodySingle.flatMap {
            krakenValidateApp(it.accessToken).map { k -> AppCredential(it, k) }
        }.doOnSuccess {
            storage.appCredential = it
        }

    override fun create(token: Token): Single<Credential> =
        validate(token).flatMap { v ->
            client.getChatUser(v.id).flatMap { gus ->
                krakenValidate(token)
                    .map {
                        return@map if (it.isValid) Credential(token, v, gus, it)
                        else throw IllegalStateException("Token is invalid! Refreshing")
                    }.doOnSuccess { storage.add(it).subscribe() }
            }
        }.onErrorResumeNext {
            if (it is IllegalStateException) refresh(token.refreshToken) else Single.error(it)
        }

    override fun create(redirectUri: String, code: String): Single<Credential> =
        client.http.post<AccessToken>(URL.OAUTH.newBuilder().addPathSegment("token").build()) {
            addQueryParameters("client_id", client.settings.clientId)
            addQueryParameters("client_secret", client.settings.clientSecret)
            addQueryParameters("grant_type", "authorization_code")
            addQueryParameters("code", code)
            addQueryParameters("redirect_uri", redirectUri)
        }.body.flatMapSingle { at ->
            validate(at.accessToken).flatMap { v ->
                client.getChatUser(v.id).flatMap { gus ->
                    krakenValidate(at.accessToken).map {
                        Credential(at, v, gus, it)
                    }
                }
            }
        }.doOnSuccess { storage.add(it).subscribe() }

    override fun refresh(credential: Credential): Single<Credential> = refresh(credential.refreshToken)

    override fun revoke(credential: Credential): Completable =
        revoke(credential.clientId, credential.accessToken)
            .doOnComplete {
                storage.delete(credential.id).subscribe()
            }

    override fun revoke(credential: AppCredential): Completable = revoke(credential.clientId, credential.accessToken)
        .doFinally {
            storage.drop().subscribe()
        }

    private fun validate(token: String) =
        client.http.get<Validate>(URL.OAUTH.newBuilder().addPathSegment("validate").build()) {
            addHeaders("Authorization", "OAuth $token")
        }.bodySingle

    private fun validateApp(accessToken: String) =
        client.http.get<AppValidate>(URL.OAUTH.newBuilder().addPathSegment("validate").build()) {
            addHeaders("Authorization", "OAuth $accessToken")
        }.bodySingle

    private fun krakenValidate(token: String) =
        client.http.get<JsonObject>(URL.KRAKEN) {
            addHeaders("Authorization", "OAuth $token")
        }.bodySingle.map { client.http.gson.fromJson(it.get("token"), Kraken::class.java) }


    private fun krakenValidateApp(accessToken: String) =
        client.http.get<JsonObject>(URL.KRAKEN) {
            addHeaders("Authorization", "OAuth $accessToken")
        }.bodySingle.map { client.http.gson.fromJson(it.get("token"), AppKraken::class.java) }

    private fun refresh(refreshToken: String) =
        client.http.post<AccessToken>(URL.OAUTH.newBuilder().addPathSegment("token").build()) {
            addQueryParameters("client_id", client.settings.clientId)
            addQueryParameters("client_secret", client.settings.clientSecret)
            addQueryParameters("grant_type", "refresh_token")
            addQueryParameters("refresh_token", refreshToken)
        }.body.flatMapSingle { at ->
            validate(at.accessToken).flatMap { v ->
                client.getChatUser(v.id).flatMap { gus ->
                    krakenValidate(at.accessToken).map {
                        Credential(at, v, gus, it)
                    }
                }
            }
        }.doOnSuccess { storage.add(it).subscribe() }

    private fun revoke(clientId: String, accessToken: String) =
        client.http.post<Unit>(URL.OAUTH.newBuilder().addPathSegment("revoke").build()) {
            addQueryParameters("client_id", clientId)
            addQueryParameters("token", accessToken)
        }.completed

}

data class TokenImpl(
    override val accessToken: String,
    override val refreshToken: String
) : Token