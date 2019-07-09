package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest

class CollectionRequest internal constructor() : AbstractRequest {
    private var limit: Int? = null
    private var cursor: String? = null
    private var containingItem: Long? = null

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun cursor(cursor: String) = apply {
        this.cursor = cursor
    }

    fun containingItem(containingItem: Long) = apply {
        this.containingItem = containingItem
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit.toString())
        }

        if (cursor != null) {
            addQueryParameters("cursor", cursor.toString())
        }

        if (containingItem != null) {
            addQueryParameters("containing_item", "video:$containingItem")
        }
    }
}