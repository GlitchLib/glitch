package io.glitchlib.v5.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import io.glitchlib.v5.model.StreamType
import io.glitchlib.v5.model.json.Channel
import io.glitchlib.v5.model.json.Game
import java.util.*

class LiveStreamsRequest internal constructor() : AbstractRequest {
    private val channel = LinkedHashSet<Channel>()
    private var game: Game? = null
    private var langauge: Locale? = null
    private var streamType: StreamType? = null
    private var limit: Int? = null
    private var offset: Int? = null

    fun game(game: Game) = apply {
        this.game = game
    }

    fun langauge(langauge: Locale) = apply {
        this.langauge = langauge
    }

    fun streamType(streamType: StreamType) = apply {
        this.streamType = streamType
    }

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun offset(offset: Int) = apply {
        this.offset = offset
    }

    fun addChannel(vararg channels: Channel) = addChannel(channels.toSet())

    fun addChannel(languages: Collection<Channel>) = apply {
        this.channel.addAll(languages)
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (channel.isNotEmpty()) {
            addQueryParameters("channel", channel.joinToString(",") { it.id.toString() })
        }

        if (game != null) {
            addQueryParameters("game", game!!.name)
        }

        if (langauge != null) {
            addQueryParameters("language", langauge!!.language)
        }

        if (streamType != null) {
            addQueryParameters("stream_type", streamType!!.name.toLowerCase())
        }

        if (limit != null && limit!! > 0 && limit!! <= 100) {
            addQueryParameters("limit", limit!!.toString())
        }

        if (offset != null && offset!! >= 0) {
            addQueryParameters("offset", offset!!.toString())
        }
    }
}
