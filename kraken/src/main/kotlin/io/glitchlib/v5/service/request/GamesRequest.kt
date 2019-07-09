package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest

class GamesRequest internal constructor() : AbstractRequest {
    private var limit: Int? = null
    private var offset: Int? = null

    fun limit(limit: Int): GamesRequest {
        this.limit = limit
        return this
    }

    fun offset(offset: Int): GamesRequest {
        this.offset = offset
        return this
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", offset!!.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset!!.toString())
        }
    }
}
