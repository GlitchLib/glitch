package io.glitchlib.v5.service.request

import io.glitchlib.auth.Credential
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.model.VideoType
import io.glitchlib.v5.model.VideoSort
import java.util.*

// Scope.USER_READ
class FollowedVideosRequest internal constructor(private val credential: Credential) : AbstractRequest {
    //    AbstractRequest<Video, Videos>(httpClient, Routes.Companion.get("/videos/followed").newRequest()) {
    private val videoType = mutableSetOf<VideoType>()
    private val language = mutableSetOf<Locale>()
    private var limit: Int? = null
    private var offset: Int? = null
    private var sort: VideoSort? = null

    fun videoType(vararg types: VideoType) = videoType(types.toSet())

    fun videoType(types: Collection<VideoType>) = apply {
        videoType.addAll(types)
    }

    fun language(vararg languages: String) = language(languages.map(Locale::forLanguageTag))

    fun language(vararg languages: Locale) = language(Arrays.asList(*languages))

    fun language(languages: Collection<Locale>) = apply {
        language.addAll(languages)
    }

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun offset(offset: Int) = apply {
        this.offset = offset
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

        if (!videoType.isEmpty()) {
            addQueryParameters("broadcast_type", videoType.joinToString(",") { it.name }.toLowerCase())
        }

        if (!language.isEmpty()) {
            addQueryParameters("language", language.joinToString(",") { it.language }.toLowerCase())
        }

        if (sort != null) {
            addQueryParameters("sort", sort!!.name.toLowerCase())
        }
    }
}
