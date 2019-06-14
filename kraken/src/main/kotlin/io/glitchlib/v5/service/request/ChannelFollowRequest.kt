package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.v5.model.Direction

class ChannelFollowRequest internal constructor() : AbstractRequest {
    private var limit: Int? = null
    private var offset: Int? = null
    private var cursor: String? = null
    private var direction: Direction? = null

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun offset(offset: Int) = apply {
        this.offset = offset
    }

    fun cursor(cursor: String) = apply {
        this.cursor = cursor
    }

    fun direction(direction: Direction) = apply {
        this.direction = direction
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset.toString())
        }

        if (direction != null) {
            addQueryParameters("direction", direction!!.name.toLowerCase())
        }
    }
}
