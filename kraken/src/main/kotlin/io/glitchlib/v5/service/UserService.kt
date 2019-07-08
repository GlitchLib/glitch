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
import io.glitchlib.v5.internal.model.json.AuthUserImpl
import io.glitchlib.v5.internal.model.json.UserImpl
import io.glitchlib.v5.model.json.AuthUser
import io.glitchlib.v5.model.json.EmoteSet
import io.glitchlib.v5.model.json.EmoteSets
import io.glitchlib.v5.model.json.Subscriber
import io.glitchlib.v5.model.json.User
import io.glitchlib.v5.model.json.UserFollow
import io.glitchlib.v5.service.request.UserFollowsRequest
import io.reactivex.Single
import io.reactivex.rxkotlin.cast

class UserService(client: GlitchClient) : AbstractKrakenService(client) {

    fun getUser(credential: Credential): Single<AuthUser> =
            if (credential.scopeCheck(Scope.USER_READ))
                get<AuthUserImpl>("/user") {
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.bodySingle.cast()
            else scopeIsMissing(Scope.USER_READ)

    fun getUserById(id: Long): Single<User> =
            get<UserImpl>("/users/$id").bodySingle.cast()

    fun getUsers(vararg logins: String) = getUsers(logins.toSet())

    fun getUsers(logins: Collection<String>) =
            get<OrdinalList<User>>("/users") {
                addQueryParameters("login", logins.chunked(100)[0].joinToString(","))
            }.bodyFlowable

    fun getUserEmotes(credential: Credential, id: Long) =
            if (credential.scopeCheck(Scope.USER_SUBSCRIPTIONS))
                get<EmoteSets>("/users/$id/emotes") {
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.bodySingle.flattenAsFlowable { it.toEmoteSets() }
            else scopeIsMissing<EmoteSet>(Scope.USER_SUBSCRIPTIONS).toFlowable()

    fun checkUserSubscriptionByChannel(credential: Credential, userId: Long, channelId: Long) =
            if (credential.scopeCheck(Scope.USER_SUBSCRIPTIONS))
                get<Subscriber>("/users/$userId/subscriptions/$channelId") {
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.bodySingle
            else scopeIsMissing(Scope.USER_SUBSCRIPTIONS)

    fun getUserFollows(id: Long, request: UserFollowsRequest.() -> Unit = {}) =
            get<CursorList<UserFollow>>("/users/$id/follows/channels", UserFollowsRequest().apply(request)()).bodySingle

    fun getFollow(userId: Long, channelId: Long) =
            get<UserFollow>("/users/$userId/follows/channels/$channelId").bodySingle

    fun followChannel(credential: Credential, channelId: Long, notifications: Boolean = false) =
            if (credential.scopeCheck(Scope.USER_FOLLOWS_EDIT))
                put<UserFollow>("/users/${credential.id}/follows/channels/$channelId") {
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                    if (notifications) {
                        addQueryParameters("notifications", notifications.toString())
                    }
                }.bodySingle
            else scopeIsMissing(Scope.USER_FOLLOWS_EDIT)


    fun unfollowChannel(credential: Credential, channelId: Long) =
            if (credential.scopeCheck(Scope.USER_FOLLOWS_EDIT))
                delete<Unit>("/users/${credential.id}/follows/channels/$channelId") {
                    addHeaders("Authorization", "OAuth ${credential.accessToken}")
                }.completed
            else scopeIsMissing<Unit>(Scope.USER_FOLLOWS_EDIT).ignoreElement()

    // TODO: Block List & VHS - https://dev.twitch.tv/docs/v5/reference/users/#get-user-block-list
}
