package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.model.VideoType
import io.glitchlib.v5.model.VideoPeriod
import io.glitchlib.v5.model.VideoSort
import io.glitchlib.v5.model.json.Game
import java.util.Locale

class TopVideosRequest internal constructor() : AbstractRequest {
    //    AbstractRequest<Video, Videos>(httpClient, Routes.Companion.get("/videos/top").newRequest()) {
    private val videoType = mutableSetOf<VideoType>()
    private val language = mutableSetOf<Locale>()
    private var limit: Int? = null
    private var offset: Int? = null
    private var game: Game? = null
    private var period: VideoPeriod? = null
    private var sort: VideoSort? = null

    fun addVideoType(vararg types: VideoType) = addVideoType(types.toSet())

    fun addVideoType(types: Collection<VideoType>) = apply {
        videoType.addAll(types)
    }

    fun addLanguage(vararg languages: String) = addLanguage(languages.map(Locale::forLanguageTag))

    fun addLanguage(vararg languages: Locale) = addLanguage(languages.toList())

    fun addLanguage(languages: Collection<Locale>) = apply {
        language.addAll(languages)
    }

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun offset(offset: Int) = apply {
        this.offset = offset
    }

    fun game(game: Game) = apply {
        this.game = game
    }

    fun period(period: VideoPeriod) = apply {
        this.period = period
    }

    fun sort(sort: VideoSort) = apply {
        this.sort = sort
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit!!.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset!!.toString())
        }

        if (game != null) {
            addQueryParameters("game", game!!.name)
        }

        if (period != null) {
            addQueryParameters("period", period!!.name.toLowerCase())
        }

        if (videoType.isNotEmpty()) {
            addQueryParameters("broadcast_type", videoType.joinToString(",") { it.name }.toLowerCase())
        }

        if (language.isNotEmpty()) {
            addQueryParameters("language", language.joinToString(",") { it.language }.toLowerCase())
        }

        if (sort != null) {
            addQueryParameters("sort", sort!!.name.toLowerCase())
        }
    }
}
