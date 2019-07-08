package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.internal.model.json.TeamImpl
import io.glitchlib.v5.internal.model.json.TeamItemImpl
import io.glitchlib.v5.model.json.Team
import io.glitchlib.v5.model.json.TeamItem
import io.glitchlib.v5.service.request.TeamsRequest
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.cast

class TeamService(client: GlitchClient) : AbstractKrakenService(client) {

    fun getAllTeams(request: TeamsRequest.() -> Unit = {}): Flowable<TeamItem> =
            get<OrdinalList<TeamItemImpl>>("/teams", TeamsRequest().apply(request)()).bodyFlowable.cast()

    fun getTeam(name: String): Single<Team> =
            get<TeamImpl>("/teams/$name").bodySingle.cast()
}
