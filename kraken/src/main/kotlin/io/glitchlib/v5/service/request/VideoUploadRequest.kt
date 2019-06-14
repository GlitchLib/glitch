package io.glitchlib.v5.service.request

import io.glitchlib.auth.Credential
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.v5.model.json.Game
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class VideoUploadRequest internal constructor(
    private val credential: Credential,
    private val channelId: Long,
    private val title: String
) : AbstractRequest {

    private var description: String? = null
    private var game: Game? = null
    private var language: Locale? = null
    private val tags = mutableSetOf<String>()
    private var viewable: Boolean? = null
    private var viewableAt: Date? = null

    fun description(description: String) = apply {
        this.description = description
    }

    fun game(game: Game) = apply {
        this.game = game
    }

    fun language(language: Locale) = apply {
        this.language = language
    }

    fun tags(vararg tags: String) = tags(tags.toSet())

    fun tags(tags: Collection<String>) = apply {
        this.tags += tags
    }

    fun viewable(viewable: Boolean) = apply {
        this.viewable = viewable
    }

    fun viewableAt(viewableAt: Date) = apply {
        this.viewableAt = viewableAt
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        addQueryParameters("channel", channelId.toString())
        addQueryParameters("title", title.substring(0..100))
        addHeaders("Authorization", "OAuth ${credential.accessToken}")

        if (description != null) addQueryParameters("description", description!!)
        if (game != null) addQueryParameters("game", game!!.name)
        if (language != null) addQueryParameters("language", language!!.language)
        if (tags.isNotEmpty()) {
            addQueryParameters("tag_list", tags.filter { it.length <= 100 }.joinToString(",").let {
                it.substring(0..it.lastIndexOf(',', 500))
            })
        }
        if (viewable != null) {
            addQueryParameters("viewable", if (viewable!!) "public" else "private")
            if (!viewable!! && viewableAt != null) {
                addQueryParameters("viewable_at", SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.format(viewableAt!!))
            }
        }
    }
}
