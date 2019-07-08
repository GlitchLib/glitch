package io.glitchlib.api.model

import java.util.*

data class WebhookEndpoint(
        private val topic: String,
        private val callback: String,
        private val expiresAt: Date
)
