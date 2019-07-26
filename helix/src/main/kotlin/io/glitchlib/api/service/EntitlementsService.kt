package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.Code
import io.glitchlib.api.model.ManifestBody
import io.glitchlib.api.model.UploadData
import io.glitchlib.auth.AppCredential
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.internal.http.completed
import io.glitchlib.model.OrdinalList

class EntitlementsService(client: GlitchClient) : AbstractHelixService(client) {

    /**
     *
     * @see [Notify the Viewer about Drops](https://dev.twitch.tv/docs/drops/#next-notify-the-viewer-about-drops)
     */
    fun uploadEntitlement(credential: AppCredential, manifestId: String, manifest: ManifestBody) =
        post<UploadData>("/entitlements/upload") {
            addHeaders("Authorization", "Bearer ${credential.accessToken}")

            addQueryParameters("manifest_id", manifestId)
            addQueryParameters("type", "bulk_drops_grant")
        }.bodySingle.flatMapCompletable {
            createTempFile("manifest-temp", ".json").apply {
                writeText((client as GlitchClientImpl).http.gson.toJson(manifest))
            }.let { f ->
                put<Unit>(it.url.replace("\\u0026", "&")) {
                    setBody(f)
                }.completed
            }
        }

    fun getStatusCode(userId: Long, vararg code: String) =
        getStatusCode(userId, code.toSet())

    fun getStatusCode(userId: Long, code: Collection<String>) =
        get<OrdinalList<Code>>("/entitlements/codes") {
            addQueryParameters("user_id", userId.toString())
            addQueryParameters("code", *code.toList().chunked(20)[0].toTypedArray())
        }.bodyFlowable

    fun redeemCode(credential: AppCredential, userId: Long, vararg code: String) =
        redeemCode(credential, userId, code.toSet())

    fun redeemCode(credential: AppCredential, userId: Long, code: Collection<String>) =
        post<OrdinalList<Code>>("/entitlements/codes") {
            addHeaders("Authorization", "Bearer ${credential.accessToken}")
            addQueryParameters("user_id", userId.toString())
            addQueryParameters("code", *code.toList().subList(0, 19).toTypedArray())
        }.bodyFlowable

}
