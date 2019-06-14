package io.glitchlib.api.service.request

import io.glitchlib.api.internal.toRfc3339
import io.glitchlib.api.model.AnalyticsReportType
import io.glitchlib.auth.Credential
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import java.util.Date

class ExtensionAnalyticsRequest internal constructor(
    private val credential: Credential
) : AbstractRequest {

    private var after: String? = null
    private var before: String? = null
    private var startedAt: Date? = null
    private var endedAt: Date? = null
    private var extensionId: String? = null
    private var limit: Int? = null
    private var type: AnalyticsReportType? = null

    fun after(after: String) = apply {
        this.after = after
    }

    fun before(before: String) = apply {
        this.before = before
    }

    fun startedAt(startedAt: Date) = apply {
        this.startedAt = startedAt
    }

    fun endedAt(endedAt: Date) = apply {
        this.endedAt = endedAt
    }

    fun extensionId(extensionId: String) = apply {
        this.extensionId = extensionId
    }

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun type(type: AnalyticsReportType) = apply {
        this.type = type
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        addHeaders("Authorization", "Bearer ${credential.accessToken}")

        if (after != null) addQueryParameters("after", after!!)

        if (before != null) addQueryParameters("before", before!!)

        if (endedAt != null) addQueryParameters("ended_at", endedAt!!.toRfc3339())

        if (extensionId != null) addQueryParameters("extension_id", extensionId!!)

        if (limit != null && limit!! in 1..100) addQueryParameters("first", limit!!.toString())

        if (startedAt != null) addQueryParameters("started_at", startedAt!!.toRfc3339())

        if (type != null) addQueryParameters("type", type!!.name.toLowerCase())
    }
}