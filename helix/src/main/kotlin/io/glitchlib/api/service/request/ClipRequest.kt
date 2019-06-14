package io.glitchlib.api.service.request

import io.glitchlib.api.internal.toRfc3339
import io.glitchlib.api.model.Game
import io.glitchlib.api.model.User
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import java.util.Date

class ClipRequest : AbstractRequest {
    private val user: User?
    private val game: Game?

    private var after: String? = null
    private var before: String? = null
    private var limit: Int? = null

    private var startedAt: Date? = null
    private var endedAt: Date? = null

    internal constructor(user: User) {
        this.user = user
        this.game = null
    }

    internal constructor(game: Game) {
        this.game = game
        this.user = null
    }

    fun after(after: String) = apply {
        this.after = after
    }

    fun before(before: String) = apply {
        this.before = before
    }

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun startedAt(startedAt: Date) = apply {
        this.startedAt = startedAt
    }

    fun endedAt(endedAt: Date) = apply {
        this.endedAt = endedAt
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (user != null) addQueryParameters("broadcaster_id", user.id.toString())

        if (game != null) addQueryParameters("game_id", game.id.toString())

        if (after != null) addQueryParameters("after", after!!)

        if (before != null) addQueryParameters("before", before!!)

        if (endedAt != null) addQueryParameters("ended_at", endedAt!!.toRfc3339())

        if (limit != null && limit!! in 1..100) addQueryParameters("first", limit!!.toString())

        if (startedAt != null) addQueryParameters("started_at", startedAt!!.toRfc3339())
    }
}