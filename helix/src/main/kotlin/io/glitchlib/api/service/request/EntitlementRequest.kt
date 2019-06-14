package io.glitchlib.api.service.request

import io.glitchlib.api.model.ManifestBody
import io.glitchlib.auth.AppCredential
import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import java.io.File

class EntitlementRequest internal constructor(private val credential: AppCredential) : AbstractRequest {
    private lateinit var manifestId: String

    internal lateinit var manifest: ManifestBody

    fun manifestId(manifestId: String) = apply {
        this.manifestId = manifestId
    }

    fun manifest(manifest: ManifestBody) = apply {
        this.manifest = manifest
    }

    override fun invoke(): HttpRequest.() -> Unit = {
    }

}