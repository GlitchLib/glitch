package io.glitchlib.api.service.request

import io.glitchlib.api.internal.toRfc3339
import io.glitchlib.api.model.AnalyticsReportType
import io.glitchlib.auth.Credential
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import java.util.Date

class GameAnalyticsRequest internal constructor(
    private val credential: Credential
) : AbstractRequest {

    private var after: String? = null
    private var before: String? = null
    private var startedAt: Date? = null
    private var endedAt: Date? = null
    private var gameId: Long? = null
    private var limit: Int? = null
    private var type: AnalyticsReportType? = null

    fun after(after: String) = apply {
        this.after = after
    }

    fun before(before: String) = apply {
        this.before = before
    }

    fun startedAt(startedAt: Date) = apply {
        this.startedAt = startedAt
    }

    fun endedAt(endedAt: Date) = apply {
        this.endedAt = endedAt
    }

    fun gameId(gameId: Long) = apply {
        this.gameId = gameId
    }

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun type(type: AnalyticsReportType) = apply {
        this.type = type
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        addHeaders("Authorization", "Bearer ${credential.accessToken}")

        if (after != null) {
            addQueryParameters("after", after!!)
        }

        if (endedAt != null) {
            addQueryParameters("ended_at", endedAt!!.toRfc3339())
        }

        if (gameId != null) {
            addQueryParameters("game_id", gameId!!.toString())
        }

        if (limit != null && limit!! in 1..100) {
            addQueryParameters("first", limit!!.toString())
        }

        if (startedAt != null) {
            addQueryParameters("started_at", startedAt!!.toRfc3339())
        }

        if (type != null) {
            addQueryParameters("type", type!!.name.toLowerCase())
        }
    }

}