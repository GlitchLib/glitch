package io.glitchlib.api.model

import io.glitchlib.model.SubscriptionType

data class Subscribe(
    val broadcasterId: Long,
    val broadcasterName: String,
    val isGift: Boolean,
    val tier: SubscriptionType,
    val planName: String,
    val userId: Long,
    val userName: String
)