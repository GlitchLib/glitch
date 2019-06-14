package io.glitchlib.api.service

import com.google.gson.JsonObject
import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.Extension
import io.glitchlib.api.model.Follow
import io.glitchlib.api.model.User
import io.glitchlib.api.model.UserExtensionComponent
import io.glitchlib.api.service.request.UserFollowRequest
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.internal.http.bodySingleFirst
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList

class UsersService(client: GlitchClient) : AbstractHelixService(client) {
    fun get(vararg id: Long) = get(null, *id)
    fun get(credential: Credential? = null, vararg id: Long) = get(credential, id = id.toSet())

    fun get(vararg login: String) = get(null, *login)
    fun get(credential: Credential? = null, vararg login: String) = get(credential, login = login.toSet())

    fun get(credential: Credential? = null, id: Collection<Long> = setOf(), login: Collection<String> = setOf()) =
        get<OrdinalList<User>>("/users") {
            if (credential != null && credential.scopeCheck(Scope.USER_READ_EMAIL)) {
                addHeaders("Authorization", "Bearer ${credential.accessToken}")
            }

            if (id.isNotEmpty()) {
                addHeaders("id", *id.toList().subList(0, 99).map(Long::toString).toTypedArray())
            }

            if (login.isNotEmpty()) {
                addHeaders("login", *login.toList().subList(0, 99).toTypedArray())
            }
        }.bodyFlowable

    fun getFollows(request: UserFollowRequest.() -> Unit = {}) =
        get<CursorList<Follow>>("/users/follows", UserFollowRequest().apply(request)()).bodySingle

    fun updateDescription(credential: Credential, description: String) =
        if (credential.scopeCheck(Scope.USER_EDIT))
            put<OrdinalList<User>>("/users") {
                addHeaders("Authorization", "Bearer ${credential.accessToken}")
                addQueryParameters("description", description)
            }.bodySingleFirst
        else scopeIsMissing(Scope.USER_EDIT)

    fun getExtensions(credential: Credential) =
        if (credential.scopeCheck(Scope.USER_READ_BROADCAST))
            get<OrdinalList<Extension>>("/users/extensions/list") {
                addHeaders("Authorization", "Bearer ${credential.accessToken}")
            }.bodyFlowable
        else scopeIsMissing<Extension>(Scope.USER_READ_BROADCAST).toFlowable()

    fun getActiveExtensions(userId: Long) =
        get<JsonObject>("/users/extensions") {
            addQueryParameters("user_id", userId.toString())
        }.bodySingle.map {
            (client as GlitchClientImpl).http.gson.fromJson(it.get("data"), UserExtensionComponent::class.java)
        }

    fun getActiveExtensions(credential: Credential) =
        if (credential.scopeCheck(Scope.USER_READ_BROADCAST) || credential.scopeCheck(Scope.USER_EDIT_BROADCAST))
            get<JsonObject>("/users/extensions") {
                addHeaders("Authorization", "Bearer ${credential.accessToken}")
            }.bodySingle.map {
                (client as GlitchClientImpl).http.gson.fromJson(it.get("data"), UserExtensionComponent::class.java)
            }
        else getActiveExtensions(credential.id)

    fun updateExtension(credential: Credential, body: UserExtensionComponent) =
        if (credential.scopeCheck(Scope.USER_EDIT_BROADCAST))
            put<JsonObject>("/users/extensions") {
                setBody(body)
                addHeaders("Authorization", "Bearer ${credential.accessToken}")
            }.bodySingle.map {
                (client as GlitchClientImpl).http.gson.fromJson(it.get("data"), UserExtensionComponent::class.java)
            }
        else scopeIsMissing(Scope.USER_EDIT_BROADCAST)
}
