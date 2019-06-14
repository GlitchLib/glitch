package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.WebhookEndpoint
import io.glitchlib.api.service.request.WebhookRequest
import io.glitchlib.auth.AppCredential
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.CursorList

class WebhooksService(client: GlitchClient) : AbstractHelixService(client) {
    fun get(credential: AppCredential, request: WebhookRequest.() -> Unit = {}) =
            get<CursorList<WebhookEndpoint>>("/webhooks/subscriptions", WebhookRequest(credential).apply(request)())
                .bodySingle
}
