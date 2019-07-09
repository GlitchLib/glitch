package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.Subscribe
import io.glitchlib.api.service.request.SubscriptionRequest
import io.glitchlib.auth.Credential
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList

class SubscriptionsService(client: GlitchClient) : AbstractHelixService(client) {
    fun get(credential: Credential, request: SubscriptionRequest.() -> Unit) =
            get<CursorList<Subscribe>>("/subscriptions", SubscriptionRequest(credential).apply(request)()).bodySingle

    fun get(credential: Credential, vararg userId: Long) = get(credential, userId.toSet())
    fun get(credential: Credential, userId: Collection<Long>) =
            get<OrdinalList<Subscribe>>("/subscriptions") {
                addHeaders("Authorization", "Bearer ${credential.accessToken}")
                addQueryParameters("broadcaster_id", credential.id.toString())
                addQueryParameters("user_id", *userId.toList().subList(0, 99).map(Long::toString).toTypedArray())
            }.bodyFlowable
}
