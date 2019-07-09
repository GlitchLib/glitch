package io.glitchlib.api.service.request

import io.glitchlib.api.internal.toRfc3339
import io.glitchlib.api.model.Period
import io.glitchlib.auth.Credential
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import java.util.*

class BitsLeaderboardRequest internal constructor(
        private val credential: Credential
) : AbstractRequest {
    private var limit: Int? = null
    private var period: Period? = null
    private var startedAt: Date? = null
    private var userId: Long? = null

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun period(period: Period) = apply {
        this.period = period
    }

    fun startedAt(startedAt: Date) = apply {
        this.startedAt = startedAt
    }

    fun userId(userId: Long) = apply {
        this.userId = userId
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        addHeaders("Authorization", "Bearer ${credential.accessToken}")

        if (limit != null && limit!! in 1..100) {
            addQueryParameters("count", limit!!.toString())
        }

        if (period != null) {
            addQueryParameters("period", period!!.name.toLowerCase())
        }

        if (startedAt != null) {
            addQueryParameters("started_at", startedAt!!.toRfc3339())
        }

        if (userId != null) {
            addQueryParameters("user_id", userId!!.toString())
        }
    }
}