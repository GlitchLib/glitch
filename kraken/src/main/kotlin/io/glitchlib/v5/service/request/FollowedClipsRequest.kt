package io.glitchlib.v5.service.request

import io.glitchlib.auth.Credential
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest

class FollowedClipsRequest(private val credential: Credential) : AbstractRequest {
    private var cursor: String? = null
    private var limit: Long? = null
    private var trending: Boolean? = null

    fun cursor(cursor: String) = apply {
        this.cursor = cursor
    }

    fun limit(limit: Long) = apply {
        this.limit = limit
    }

    fun trending(trending: Boolean) = apply {
        this.trending = trending
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (cursor != null) {
            addQueryParameters("cursor", cursor!!)
        }

        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit.toString())
        }

        if (trending != null && trending!!) {
            addQueryParameters("trending", trending.toString())
        }

        addHeaders("Authroization", "OAuth ${credential.accessToken}")
    }
}
