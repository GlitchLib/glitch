package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest

class TeamsRequest internal constructor() : AbstractRequest {
    //    AbstractRequest<Team, Teams>(httpClient, Routes.Companion.get("/teams").newRequest()) {
    private var limit: Int? = null
    private var offset: Int? = null

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun offset(offset: Int) = apply {
        this.offset = offset
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit!!.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset!!.toString())
        }

    }
}
