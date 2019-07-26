package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.model.json.Channel
import io.glitchlib.v5.model.json.Game
import io.glitchlib.v5.model.json.Stream
import io.glitchlib.v5.service.request.ChannelSearchRequest
import io.glitchlib.v5.service.request.GamesSearchRequest
import io.glitchlib.v5.service.request.StreamSearchRequest

class SearchService(client: GlitchClient) : AbstractKrakenService(client) {

    fun searchChannel(query: String, request: ChannelSearchRequest.() -> Unit = {}) =
        get<OrdinalList<Channel>>("/search/channels", ChannelSearchRequest(query).apply(request)()).bodyFlowable

    fun searchGames(query: String, request: GamesSearchRequest.() -> Unit = {}) =
        get<OrdinalList<Game>>("/search/games", GamesSearchRequest(query).apply(request)()).bodyFlowable

    fun searchStreams(query: String, request: StreamSearchRequest.() -> Unit = {}) =
        get<OrdinalList<Stream>>("/search/streams", StreamSearchRequest(query).apply(request)()).bodyFlowable
}
