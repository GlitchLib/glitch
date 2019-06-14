package io.glitchlib.api.model

import java.util.Date

data class WebhookEndpoint(
    private val topic: String,
    private val callback: String,
    private val expiresAt: Date
)
