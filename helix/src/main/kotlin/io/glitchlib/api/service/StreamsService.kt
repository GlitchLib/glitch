package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.MarkerCreate
import io.glitchlib.api.model.Stream
import io.glitchlib.api.model.StreamMarkerData
import io.glitchlib.api.model.User
import io.glitchlib.api.model.Video
import io.glitchlib.api.service.request.StreamMarkerRequest
import io.glitchlib.api.service.request.StreamRequest
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList

class StreamsService(client: GlitchClient) : AbstractHelixService(client) {
    fun get(request: StreamRequest.() -> Unit = {}) =
        get<CursorList<Stream>>("/streams", StreamRequest().apply(request)()).bodySingle

    fun getMetadata(request: StreamRequest.() -> Unit = {}) =
        get<CursorList<Stream>>("/streams", StreamRequest().apply(request)()).bodySingle

    fun createMarker(credential: Credential, id: Long, description: String? = null) =
        if (credential.scopeCheck(Scope.USER_EDIT_BROADCAST))
            get<OrdinalList<MarkerCreate>>("/streams/markers") {
                addHeaders("Authorization", "Bearer ${credential.accessToken}")
                addQueryParameters("user_id", id.toString())
                if (description != null) {
                    addQueryParameters("description", description)
                }
            }.bodySingle.map { it.data[0] }
        else scopeIsMissing(Scope.USER_EDIT_BROADCAST)

    fun getMarkers(credential: Credential, video: Video, request: StreamMarkerRequest.() -> Unit = {}) =
        if (credential.scopeCheck(Scope.USER_READ_BROADCAST))
            get<CursorList<StreamMarkerData>>(
                "/streams",
                StreamMarkerRequest(credential, video).apply(request)()
            ).bodySingle
        else scopeIsMissing(Scope.USER_READ_BROADCAST)

    fun getMarkers(credential: Credential, user: User, request: StreamMarkerRequest.() -> Unit = {}) =
        if (credential.scopeCheck(Scope.USER_READ_BROADCAST))
            get<CursorList<StreamMarkerData>>(
                "/streams",
                StreamMarkerRequest(credential, user).apply(request)()
            ).bodySingle
        else scopeIsMissing(Scope.USER_READ_BROADCAST)
}
