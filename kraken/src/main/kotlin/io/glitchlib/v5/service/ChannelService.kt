package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.internal.http.completed
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.internal.model.json.AuthChannelImpl
import io.glitchlib.v5.internal.model.json.ChannelFollows
import io.glitchlib.v5.internal.model.json.ChannelImpl
import io.glitchlib.v5.internal.model.json.Editors
import io.glitchlib.v5.model.json.AuthChannel
import io.glitchlib.v5.model.json.Channel
import io.glitchlib.v5.model.json.ChannelBody
import io.glitchlib.v5.model.json.CommercialData
import io.glitchlib.v5.model.json.Community
import io.glitchlib.v5.model.json.Subscriber
import io.glitchlib.v5.model.json.TeamItem
import io.glitchlib.v5.model.json.Video
import io.glitchlib.v5.service.request.ChannelFollowRequest
import io.glitchlib.v5.service.request.ChannelSubscribersRequest
import io.glitchlib.v5.service.request.ChannelVideoRequest
import io.glitchlib.v5.service.request.CollectionRequest
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.cast
import java.util.*

class ChannelService internal constructor(client: GlitchClient) : AbstractKrakenService(client) {
    fun getChannel(credential: Credential) = if (credential.scopeCheck(Scope.CHANNEL_READ))
        get<AuthChannelImpl>("/channel") {
            addHeaders("Authorization", "OAuth ${credential.accessToken}")
        }.bodySingle.cast<AuthChannel>()
    else scopeIsMissing(Scope.CHANNEL_READ)

    fun getChannel(id: Long) =
            get<ChannelImpl>("/channels/$id").bodySingle.cast<Channel>()

    fun updateChannel(credential: Credential, id: Long, data: ChannelBody.() -> Unit = {}) =
            if (credential.scopeCheck(Scope.CHANNEL_EDITOR))
                ChannelBody().apply(data).apply {
                    if (id != credential.id) {
                        delay = null
                    }
                }.let {
                    put<AuthChannelImpl>("/channels/$id") {
                        addHeaders("Authorization", "OAuth ${credential.accessToken}")
                        setBody(mapOf("channel" to it))
                    }.bodySingle.cast<AuthChannel>()
                }
            else scopeIsMissing(Scope.CHANNEL_EDITOR)

    fun getChannelEditors(credential: Credential, id: Long) =
            if (credential.scopeCheck(Scope.CHANNEL_READ))
                get<Editors>("/channels/$id/editors") {
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.bodySingle
            else scopeIsMissing(Scope.CHANNEL_READ)

    fun getChannelFollows(id: Long, request: ChannelFollowRequest.() -> Unit = {}) =
            get<ChannelFollows>("/channels/$id/editors", ChannelFollowRequest().apply(request)()).bodySingle

    fun getChannelTeams(id: Long) =
            get<OrdinalList<TeamItem>>("/channels/$id/teams").bodyFlowable

    fun getChannelSubscribers(credential: Credential, id: Long, request: ChannelSubscribersRequest.() -> Unit = {}) =
            if (credential.scopeCheck(Scope.CHANNEL_SUBSCRIPTIONS))
                get<OrdinalList<Subscriber>>(
                        "/channels/$id/subscriptions",
                        ChannelSubscribersRequest(credential).apply(request)()
                ).bodyFlowable
            else scopeIsMissing<Subscriber>(Scope.CHANNEL_SUBSCRIPTIONS).toFlowable()

    fun checkChannelSubscriptionByUser(credential: Credential, channelId: Long, userId: Long) =
            if (credential.scopeCheck(Scope.CHANNEL_SUBSCRIPTIONS))
                get<Subscriber>("/channels/$channelId/subscriptions/$userId") {
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.flatMapMaybe {
                    if (it.status.isError && it.status.code == 404) Maybe.error(NullPointerException(it.status.message))
                    else it.body
                } else scopeIsMissing<Subscriber>(Scope.CHANNEL_SUBSCRIPTIONS).toMaybe()

    fun getChannelVideos(id: Long, request: ChannelVideoRequest.() -> Unit = {}) =
            get<OrdinalList<Video>>("/channels/$id/videos", ChannelVideoRequest().apply(request)())

    fun startChannelCommercial(id: Long, credential: Credential, duration: Int) =
            if (credential.scopeCheck(Scope.CHANNEL_COMMERCIAL))
                post<CommercialData>("/channels/$id/commercial") {
                    setBody(mapOf("length" to (COMMERCIAL_DURATION.minBy { Math.abs(it - duration) } ?: 30).toString()))
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.bodySingle
            else scopeIsMissing<CommercialData>(Scope.CHANNEL_COMMERCIAL)

    fun resetStreamKey(id: Long, credential: Credential): Single<AuthChannel> =
            if (credential.scopeCheck(Scope.CHANNEL_STREAM))
                delete<AuthChannelImpl>("/channels/$id/stream_key") {
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.bodySingle.cast()
            else scopeIsMissing(Scope.CHANNEL_STREAM)

    fun getChannelCommunities(id: Long) =
            get<OrdinalList<Community>>("/channels/$id/communities").bodyFlowable

    fun setChannelCommunities(id: Long, credential: Credential, vararg communityIds: String) =
            setChannelCommunities(id, credential, *communityIds.map(UUID::fromString).toTypedArray())

    fun setChannelCommunities(id: Long, credential: Credential, vararg communityIds: UUID) =
            setChannelCommunities(id, credential, communityIds.toSet())

    fun setChannelCommunities(id: Long, credential: Credential, communityIds: Collection<UUID>) =
            if (credential.scopeCheck(Scope.CHANNEL_EDITOR))
                put<Unit>("/channels/$id/communities") {
                    setBody(mapOf("community_ids" to communityIds.map { it.toString() }.toSet()))
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.completed
            else scopeIsMissing<Unit>(Scope.CHANNEL_EDITOR).ignoreElement()

    fun clearChannelCommunities(id: Long, credential: Credential, communityIds: Collection<UUID>) =
            if (credential.scopeCheck(Scope.CHANNEL_EDITOR))
                delete<Unit>("/channels/$id/communities") {
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.completed
            else scopeIsMissing<Unit>(Scope.CHANNEL_EDITOR).ignoreElement()

    fun getCollections(id: Long, request: CollectionRequest.() -> Unit = {}) =
            get<CursorList<io.glitchlib.v5.model.json.Collection>>(
                    "/channels/$id/collections",
                    CollectionRequest().apply(request).invoke()
            ).bodySingle

    private val COMMERCIAL_DURATION = setOf(30, 60, 90, 120, 150, 180)
}
