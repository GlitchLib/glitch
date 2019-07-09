package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.Clip
import io.glitchlib.api.model.ClipCreate
import io.glitchlib.api.model.Game
import io.glitchlib.api.model.User
import io.glitchlib.api.service.request.ClipRequest
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList

class ClipsService(client: GlitchClient) : AbstractHelixService(client) {
    fun createClip(credential: Credential, userId: Long, delay: Boolean = false) =
        if (credential.scopeCheck(Scope.CLIPS_EDIT))
            post<OrdinalList<ClipCreate>>("/clips") {
                addHeaders("Authorization", "Bearer ${credential.accessToken}")
                addQueryParameters("broadcaster_id", userId.toString())
                if (delay) {
                    addQueryParameters("has_delay", delay.toString())
                }
            }.bodySingle.map { it.data[0] }
        else scopeIsMissing(Scope.CLIPS_EDIT)

    fun get(user: User, request: ClipRequest.() -> Unit = {}) =
        get<CursorList<Clip>>("/clips", ClipRequest(user).apply(request)()).bodySingle

    fun get(game: Game, request: ClipRequest.() -> Unit = {}) =
        get<CursorList<Clip>>("/clips", ClipRequest(game).apply(request)()).bodySingle

    fun get(vararg id: String) = get(id.toSet())
    fun get(id: Collection<String>) =
        get<OrdinalList<Clip>>("/clips") {
            addQueryParameters("id", *id.toList().subList(0, 99).toTypedArray())
        }.bodyFlowable
}
