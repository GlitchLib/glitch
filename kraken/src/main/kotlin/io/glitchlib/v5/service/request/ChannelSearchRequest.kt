package io.glitchlib.v5.service.request

import com.google.common.net.UrlEscapers
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest

class ChannelSearchRequest internal constructor(private val query: String) : AbstractRequest {
    private var offset: Int? = null
    private var limit: Int? = null


    fun offset(offset: Int) = apply {
        this.offset = offset
    }

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        addQueryParameters("query", UrlEscapers.urlPathSegmentEscaper().escape(query))

        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit!!.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset!!.toString())
        }
    }
}