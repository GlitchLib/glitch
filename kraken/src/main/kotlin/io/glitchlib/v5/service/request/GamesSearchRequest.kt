package io.glitchlib.v5.service.request

import com.google.common.net.UrlEscapers
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest

class GamesSearchRequest(private val query: String) : AbstractRequest {
    private var live: Boolean = false

    fun live(live: Boolean) = apply {
        this.live = live
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        addQueryParameters("query", UrlEscapers.urlPathSegmentEscaper().escape(query))

        if (live) {
            addQueryParameters("live", live.toString())
        }
    }
}
