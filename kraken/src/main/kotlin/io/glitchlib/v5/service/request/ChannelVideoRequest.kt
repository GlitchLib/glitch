package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.model.VideoType
import io.glitchlib.v5.model.VideoSort
import java.util.*

class ChannelVideoRequest internal constructor() : AbstractRequest {
    private val videoType = mutableSetOf<VideoType>()
    private val languages = mutableSetOf<Locale>()
    private var limit: Int? = null
    private var offset: Int? = null
    private var sort: VideoSort? = null

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun offset(offset: Int) = apply {
        this.offset = offset
    }

    fun sort(sort: VideoSort) = apply {
        this.sort = sort
    }

    fun addVideoType(vararg types: VideoType) = addVideoType(types.toList())

    fun addVideoType(types: Collection<VideoType>) = apply {
        videoType.addAll(types)
    }

    fun addLanguage(vararg languages: String) = addLanguage(languages.map { Locale.forLanguageTag(it) })

    fun addLanguage(vararg languages: Locale) = addLanguage(Arrays.asList(*languages))

    fun addLanguage(languages: Collection<Locale>) = apply {
        this.languages.addAll(languages)
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset.toString())
        }

        if (sort != null) {
            addQueryParameters("sort", sort!!.name.toLowerCase())
        }

        if (videoType.isNotEmpty()) {
            addQueryParameters("broadcast_type", videoType.joinToString(",") { v -> v.name.toLowerCase() })
        }

        if (languages.isNotEmpty()) {
            addQueryParameters("language", languages.joinToString(",") { it.language })
        }
    }
}
