package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.CursorList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.model.json.Clip
import io.glitchlib.v5.service.request.FollowedClipsRequest
import io.glitchlib.v5.service.request.TopClipsRequest

class ClipService(client: GlitchClient) : AbstractKrakenService(client) {

    fun getTopClips(request: TopClipsRequest.() -> Unit = {}) =
        get<CursorList<Clip>>("/clips/top", TopClipsRequest().apply(request)()).bodySingle

    fun getClip(slug: String) =
        get<Clip>("/clips/$slug").bodySingle

    fun getFollowedClips(credential: Credential, request: FollowedClipsRequest.() -> Unit = {}) =
        if (credential.scopeCheck(Scope.USER_READ))
            get<CursorList<Clip>>("/clips/followed", FollowedClipsRequest(credential).apply(request)()).bodySingle
        else scopeIsMissing(Scope.USER_READ)
}
