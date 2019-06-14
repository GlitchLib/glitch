package io.glitchlib.api.service.request

import io.glitchlib.api.model.User
import io.glitchlib.api.model.Video
import io.glitchlib.auth.Credential
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest

class StreamMarkerRequest : AbstractRequest {

    private val credential: Credential

    private val userId: Long?
    private val videoId: Long?

    private var limit: Int? = null
    private var after: String? = null
    private var before: String? = null

    internal constructor(credential: Credential, user: User) {
        this.credential = credential
        this.userId = user.id
        this.videoId = null
    }

    internal constructor(credential: Credential, video: Video) {
        this.credential = credential
        this.videoId = video.id
        this.userId = null
    }

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun after(after: String) = apply {
        this.after = after
    }

    fun before(before: String) = apply {
        this.before = before
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        addHeaders("Authorization", "Bearer ${credential.accessToken}")

        if (userId != null) addQueryParameters("user_id", userId.toString())
        if (videoId != null) addQueryParameters("video_id", videoId.toString())

        if (limit != null && limit!! in 1..100) addQueryParameters("first", limit!!.toString())
        if (after != null) addQueryParameters("after", after!!)
        if (before != null) addQueryParameters("before", before!!)
    }
}