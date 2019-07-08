package io.glitchlib.api.service.request

import io.glitchlib.api.model.Game
import io.glitchlib.api.model.Period
import io.glitchlib.api.model.User
import io.glitchlib.api.model.VideoSort
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.model.VideoType
import java.util.*

class VideoRequest : AbstractRequest {
    internal constructor(user: User) {
        this.userId = user.id
        this.gameId = null
    }

    internal constructor(game: Game) {
        this.gameId = game.id
        this.userId = null
    }

    private val userId: Long?
    private val gameId: Long?

    private var limit: Int? = null
    private var after: String? = null
    private var before: String? = null

    private var language: Locale? = null
    private var period: Period? = null
    private var sort: VideoSort? = null
    private var type: VideoType? = null

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun after(after: String) = apply {
        this.after = after
    }

    fun before(before: String) = apply {
        this.before = before
    }

    fun language(language: Locale) = apply {
        this.language = language
    }

    fun period(period: Period) = apply {
        this.period = period
    }

    fun sort(sort: VideoSort) = apply {
        this.sort = sort
    }

    fun type(type: VideoType) = apply {
        this.type = type
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (userId != null) addQueryParameters("user_id", userId.toString())
        if (gameId != null) addQueryParameters("game_id", gameId.toString())
        if (limit != null && limit!! in 1..100) addQueryParameters("first", limit!!.toString())
        if (after != null) addQueryParameters("after", after!!)
        if (before != null) addQueryParameters("before", before!!)
        if (language != null) addQueryParameters("language", language!!.language)
        if (period != null) addQueryParameters("period", period!!.name.toLowerCase())
        if (sort != null) addQueryParameters("sort", sort!!.name.toLowerCase())
        if (type != null) addQueryParameters("type", type!!.name.toLowerCase())
    }
}