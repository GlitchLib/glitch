package io.glitchlib.api.service

import io.glitchlib.GlitchClient
import io.glitchlib.api.internal.AbstractHelixService
import io.glitchlib.api.model.Tag
import io.glitchlib.api.service.request.TagRequest
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.internal.http.completed
import io.glitchlib.model.CursorList
import io.glitchlib.model.OrdinalList
import io.reactivex.Completable

class TagsService(client: GlitchClient) : AbstractHelixService(client) {
    fun get(request: TagRequest.() -> Unit = {}) =
            get<CursorList<Tag>>("/tags/streams", TagRequest().apply(request)()).bodySingle

    fun get(channelId: Long) =
            get<OrdinalList<Tag>>("/streams/tags?broadcaster_id=$channelId").bodyFlowable

    fun set(credential: Credential, vararg tags: Tag) = set(credential, tags.toSet())
    fun set(credential: Credential, tags: Collection<Tag>) = set(credential, credential.id, tags)
    fun set(credential: Credential, channelId: Long, vararg tags: Tag) = set(credential, channelId, tags.toSet())
    fun set(credential: Credential, channelId: Long, tags: Collection<Tag>): Completable =
            if (credential.scopeCheck(Scope.USER_EDIT_BROADCAST))
                put<Unit>("/streams/tags") {
                    addHeaders("Authorize", "Bearer ${credential.accessToken}")
                    addQueryParameters("broadcaster_id", channelId.toString())
                    if (tags.isNotEmpty()) {
                        setBody(mapOf("tag_ids" to tags.map { it.id.toString() }.toSet()))
                    }
                }.completed
            else scopeIsMissing<Unit>(Scope.USER_EDIT_BROADCAST).ignoreElement()

}
