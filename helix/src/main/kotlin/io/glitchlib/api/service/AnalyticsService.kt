package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.ExtensionReport
import io.glitchlib.api.model.GameReport
import io.glitchlib.api.service.request.ExtensionAnalyticsRequest
import io.glitchlib.api.service.request.GameAnalyticsRequest
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList

class AnalyticsService(client: GlitchClient) : AbstractHelixService(client) {
    fun getExtensionAnalytics(credential: Credential, request: ExtensionAnalyticsRequest.() -> Unit = {}) =
        if (credential.scopeCheck(Scope.ANALYTICS_READ_EXTENSIONS))
            get<OrdinalList<ExtensionReport>>(
                "/analytics/extensions",
                ExtensionAnalyticsRequest(credential).apply(request)()
            ).bodySingle
        else scopeIsMissing(Scope.ANALYTICS_READ_EXTENSIONS)

    fun getGameAnalytics(credential: Credential, request: GameAnalyticsRequest.() -> Unit = {}) =
        if (credential.scopeCheck(Scope.ANALYTICS_READ_GAMES))
            get<CursorList<GameReport>>(
                "/analytics/extensions",
                GameAnalyticsRequest(credential).apply(request)()
            ).bodySingle
        else scopeIsMissing(Scope.ANALYTICS_READ_GAMES)
}
