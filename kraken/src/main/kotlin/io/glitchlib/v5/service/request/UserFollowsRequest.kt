package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.v5.model.Direction
import io.glitchlib.v5.model.Sorting

class UserFollowsRequest internal constructor() : AbstractRequest {
//    AbstractRequest<UserFollow, UserChannelFollows>(http,Routes.Companion.get("/users/%s/follows/channels").newRequest(id)) {

    private var limit: Int? = null
    private var offset: Int? = null
    private var direction: Direction? = null
    private var sortBy: Sorting? = null

    fun limit(limit: Int?) = apply {
        this.limit = limit
    }

    fun offset(offset: Int?) = apply {
        this.offset = offset
    }

    fun direction(direction: Direction) = apply {
        this.direction = direction
    }

    fun sortBy(sortBy: Sorting) = apply {
        this.sortBy = sortBy
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit!!.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset!!.toString())
        }

        if (direction != null) {
            addQueryParameters("direction", direction!!.name.toLowerCase())
        }

        if (sortBy != null) {
            addQueryParameters("sortby", sortBy!!.name.toLowerCase())
        }
    }
}
