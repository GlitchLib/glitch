package io.glitchlib.v5.service.request

import io.glitchlib.auth.Credential
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.v5.model.StreamType

// Scope.USER_READ
class FollowedStreamRequest internal constructor(private val credential: Credential) : AbstractRequest {
// internal constructor(): AbstractRequest {
//    AbstractRequest<Stream, Streams>(http, Routes.Companion.get("/streams/followed").newRequest()) {

    private var streamType: StreamType? = null
    private var limit: Int? = null
    private var offset: Int? = null

    fun streamType(streamType: StreamType): FollowedStreamRequest {
        this.streamType = streamType
        return this
    }

    fun limit(limit: Int): FollowedStreamRequest {
        this.limit = limit
        return this
    }

    fun offset(offset: Int): FollowedStreamRequest {
        this.offset = offset
        return this
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        addHeaders("Authroization", "OAuth ${credential.accessToken}")

        if (streamType != null) {
            addQueryParameters("stream_type", streamType!!.name.toLowerCase())
        }

        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit!!.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset!!.toString())
        }
    }
}
