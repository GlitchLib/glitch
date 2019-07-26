package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.http.body
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.model.StreamType
import io.glitchlib.v5.model.json.FeatureStream
import io.glitchlib.v5.model.json.Game
import io.glitchlib.v5.model.json.Stream
import io.glitchlib.v5.model.json.StreamSummary
import io.glitchlib.v5.service.request.FeatureStreamsRequest
import io.glitchlib.v5.service.request.FollowedStreamRequest
import io.glitchlib.v5.service.request.LiveStreamsRequest


class StreamService(client: GlitchClient) : AbstractKrakenService(client) {

    fun getLiveStreams(request: LiveStreamsRequest.() -> Unit = {}) =
        get<OrdinalList<Stream>>("/streams", LiveStreamsRequest().apply(request)()).bodyFlowable

    fun getFreaturedStreams(request: FeatureStreamsRequest.() -> Unit = {}) =
        get<OrdinalList<FeatureStream>>("/streams/featured", FeatureStreamsRequest().apply(request)()).bodyFlowable

    fun getStreamByUser(id: Long, streamType: StreamType? = null) =
        get<OrdinalList<Stream>>("/streams/$id") {
            if (streamType != null) {
                addQueryParameters("stream_type", streamType.name.toLowerCase())
            }
        }.body.map { it.data[0] }

    fun getStreamSummary(game: Game? = null) =
        get<StreamSummary>("/streams/summary") {
            if (game != null) {
                addQueryParameters("game", game.name)
            }
        }.bodySingle

    fun getFollowedStreams(credential: Credential, request: FollowedStreamRequest.() -> Unit = {}) =
        if (credential.scopeCheck(Scope.USER_READ))
            get<OrdinalList<Stream>>(
                "/streams/followed",
                FollowedStreamRequest(credential).apply(request)()
            ).bodyFlowable
        else scopeIsMissing<Stream>(Scope.USER_READ).toFlowable()
}
