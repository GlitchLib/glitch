package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.model.json.Game
import io.glitchlib.v5.service.request.GamesRequest

class GameService(client: GlitchClient) : AbstractKrakenService(client) {
    fun getTopGames(request: GamesRequest.() -> Unit = {}) =
            get<OrdinalList<Game>>("/games/top", GamesRequest().apply(request)()).bodyFlowable
}
