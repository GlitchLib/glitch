package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.Game
import io.glitchlib.api.service.request.GameRequest
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList

class GamesService(client: GlitchClient) : AbstractHelixService(client) {
    fun top(request: GameRequest.() -> Unit = {}) =
            get<CursorList<Game>>("/games/top", GameRequest().apply(request)()).bodySingle

    fun get(vararg id: Long) = get(id = id.toSet())

    fun get(vararg name: String) = get(name = name.toSet())

    fun get(id: Collection<Long> = setOf(), name: Collection<String> = setOf()) =
            get<OrdinalList<Game>>("/games") {
                if (id.isNotEmpty()) {
                    addQueryParameters("id", *id.toList().subList(0, 99).map(Long::toString).toTypedArray())
                }

                if (name.isNotEmpty()) {
                    addQueryParameters("name", *name.toList().subList(0, 99).toTypedArray())
                }
            }.bodyFlowable

}
