package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest

class StreamSearchRequest(private val query: String) : AbstractRequest {

    private var limit: Int? = null
    private var offset: Int? = null
    private var httpLiveStreaming: Boolean = false

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun offset(offset: Int) = apply {
        this.offset = offset
    }

    fun httpLiveStreaming(httpLiveStreaming: Boolean) = apply {
        this.httpLiveStreaming = httpLiveStreaming
    }

    override fun invoke(): HttpRequest.() -> Unit = {

        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit!!.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset!!.toString())
        }

        if (httpLiveStreaming) {
            addQueryParameters("hls", httpLiveStreaming.toString())
        }
    }
}
