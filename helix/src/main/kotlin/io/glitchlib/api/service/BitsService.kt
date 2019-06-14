package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.BitsLeaderboard
import io.glitchlib.api.model.ExtensionInvoice
import io.glitchlib.api.service.request.BitsLeaderboardRequest
import io.glitchlib.api.service.request.ExtensionTransactionRequest
import io.glitchlib.auth.AppCredential
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList

class BitsService(client: GlitchClient) : AbstractHelixService(client) {
    fun getLeaderboard(credential: Credential, request: BitsLeaderboardRequest.() -> Unit = {}) =
        if (credential.scopeCheck(Scope.BITS_READ))
            get<BitsLeaderboard>("/bits/leaderboard", BitsLeaderboardRequest(credential).apply(request)()).bodySingle
        else scopeIsMissing(Scope.BITS_READ)

    fun getExtensionTransaction(credential: AppCredential, request: ExtensionTransactionRequest.() -> Unit) =
        get<CursorList<ExtensionInvoice>>(
            "/extensions/transactions",
            ExtensionTransactionRequest(credential).apply(request)()
        ).bodySingle

    fun getExtensionTransaction(credential: AppCredential, vararg id: String) =
            getExtensionTransaction(credential, id.toSet())

    fun getExtensionTransaction(credential: AppCredential, id: Collection<String>) =
        get<OrdinalList<ExtensionInvoice>>("/extensions/transactions") {
            addHeaders("Authorization", "Bearer ${credential.accessToken}")
            addQueryParameters("extension_id", credential.clientId)
            addQueryParameters("id", *id.toList().subList(0, 99).toTypedArray())
        }.bodySingle
}
