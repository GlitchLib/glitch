package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.Game
import io.glitchlib.api.model.User
import io.glitchlib.api.model.Video
import io.glitchlib.api.service.request.VideoRequest
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList

class VideosService(client: GlitchClient) : AbstractHelixService(client) {
    fun get(vararg id: Long) = get(id.toSet())
    fun get(id: Collection<Long>) =
        get<OrdinalList<Video>>("/videos") {
            addQueryParameters("id", *id.toList().subList(0, 99).map(Long::toString).toTypedArray())
        }.bodyFlowable

    fun get(game: Game, request: VideoRequest.() -> Unit = {}) =
        get<CursorList<Video>>("/videos", VideoRequest(game).apply(request)()).bodySingle

    fun get(user: User, request: VideoRequest.() -> Unit = {}) =
        get<CursorList<Video>>("/videos", VideoRequest(user).apply(request)()).bodySingle
}
