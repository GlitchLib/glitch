package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.v5.model.ClipPeriod
import io.glitchlib.v5.model.json.Channel
import io.glitchlib.v5.model.json.Game
import java.util.*

class TopClipsRequest : AbstractRequest {

    private val language = LinkedHashSet<Locale>(28)
    private var channel: Channel? = null
    private var game: Game? = null
    private var cursor: String? = null
    private var limit: Long? = null
    private var period: ClipPeriod? = null
    private var trending: Boolean? = null

    fun addLanguage(vararg language: Locale) = addLanguage(language.toList())
    fun addLanguage(vararg language: String) = addLanguage(language.map(Locale::forLanguageTag))

    fun addLanguage(language: Collection<Locale>) = apply {
        this.language.addAll(language)
    }

    fun channel(channel: Channel) = apply {
        this.channel = channel
    }

    fun game(game: Game) = apply {
        this.game = game
    }

    fun cursor(cursor: String) = apply {
        this.cursor = cursor
    }

    fun limit(limit: Long) = apply {
        this.limit = limit
    }

    fun period(period: ClipPeriod) = apply {
        this.period = period
    }

    fun trending(trending: Boolean) = apply {
        this.trending = trending
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (channel != null) {
            addQueryParameters("channel", channel!!.username)
        } else if (game != null) {
            addQueryParameters("game", game!!.name)
        }

        if (cursor != null) {
            addQueryParameters("cursor", cursor!!)
        }

        if (language.isNotEmpty() && language.size <= 28) {
            addQueryParameters("language", language.joinToString(",") { it.language })
        }

        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit.toString())
        }

        if (period != null) {
            addQueryParameters("period", period!!.name.toLowerCase())
        }

        if (trending != null && trending!!) {
            addQueryParameters("trending", trending!!.toString())
        }
    }
}
