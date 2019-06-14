package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.internal.http.completed
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.model.json.Collection

class CollectionService(client: GlitchClient) : AbstractKrakenService(client) {
    operator fun get(id: String) =
        get<Collection>("/collections/$id").bodySingle

    fun getItems(id: String, includeAllItems: Boolean = false) =
        get<OrdinalList<Collection.Item>>("/collections/$id") {
            if (includeAllItems) {
                addQueryParameters("include_all_items", includeAllItems.toString())
            }
        }.bodyFlowable

    fun create(credential: Credential, channelId: Long, title: String) =
        if (credential.scopeCheck(Scope.COLLECTIONS_EDIT))
            post<Collection>("/channels/$channelId/collections") {
                addHeaders("Authorization", "OAuth ${credential.accessToken}")
                setBody(mapOf("title" to title))
            }.bodySingle
        else scopeIsMissing(Scope.COLLECTIONS_EDIT)

    fun update(credential: Credential, id: String, title: String) =
        if (credential.scopeCheck(Scope.COLLECTIONS_EDIT))
            put<Unit>("/collections/$id") {
                addHeaders("Authorization", "OAuth ${credential.accessToken}")
                setBody(mapOf("title" to title))
            }.completed
        else scopeIsMissing<Unit>(Scope.COLLECTIONS_EDIT).ignoreElement()

    fun updateThumbnail(credential: Credential, id: String, itemId: String) =
        if (credential.scopeCheck(Scope.COLLECTIONS_EDIT))
            put<Unit>("/collections/$id/thumbnail") {
                addHeaders("Authorization", "OAuth ${credential.accessToken}")
                setBody(mapOf("item_id" to itemId))
            }.completed
        else scopeIsMissing<Unit>(Scope.COLLECTIONS_EDIT).ignoreElement()

    fun delete(credential: Credential, id: String) =
        if (credential.scopeCheck(Scope.COLLECTIONS_EDIT))
            delete<Unit>("/collections/$id") {
                addHeaders("Authorization", "OAuth ${credential.accessToken}")
            }.completed
        else scopeIsMissing<Unit>(Scope.COLLECTIONS_EDIT).ignoreElement()

    fun addItem(credential: Credential, id: String, videoId: Long) =
        if (credential.scopeCheck(Scope.COLLECTIONS_EDIT))
            post<Collection.Item>("/collections/$id/items") {
                addHeaders("Authorization", "OAuth ${credential.accessToken}")
                setBody(mapOf("id" to "$videoId", "type" to "video"))
            }.bodySingle
        else scopeIsMissing<Unit>(Scope.COLLECTIONS_EDIT)

    fun deleteItem(credential: Credential, id: String, itemId: String) =
        if (credential.scopeCheck(Scope.COLLECTIONS_EDIT))
            delete<Unit>("/collections/$id/items/$itemId") {
                addHeaders("Authorization", "OAuth ${credential.accessToken}")
            }.completed
        else scopeIsMissing<Unit>(Scope.COLLECTIONS_EDIT).ignoreElement()

    fun moveItem(credential: Credential, id: String, itemId: String, position: Int) =
        if (credential.scopeCheck(Scope.COLLECTIONS_EDIT))
            put<Unit>("/collections/$id/items/$itemId") {
                addHeaders("Authorization", "OAuth ${credential.accessToken}")
                setBody(mapOf("position" to "$position"))
            }.completed
        else scopeIsMissing<Unit>(Scope.COLLECTIONS_EDIT).ignoreElement()

}
