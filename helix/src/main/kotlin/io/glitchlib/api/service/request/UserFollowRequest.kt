package io.glitchlib.api.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest

class UserFollowRequest internal constructor() : AbstractRequest {
    private var limit: Int? = null
    private var after: String? = null
    private var before: String? = null

    private var from: Long? = null
    private var to: Long? = null

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun after(after: String) = apply {
        this.after = after
    }

    fun before(before: String) = apply {
        this.before = before
    }

    fun from(from: Long) = apply {
        this.from = from
    }

    fun to(to: Long) = apply {
        this.to = to
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (limit != null && limit!! in 1..100) addQueryParameters("first", limit!!.toString())
        if (after != null) addQueryParameters("after", after!!)
        if (before != null) addQueryParameters("before", before!!)
        if (from != null) addQueryParameters("from_id", from.toString())
        if (to != null) addQueryParameters("to_id", to.toString())
    }
}
