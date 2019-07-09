package io.glitchlib.api.model

import com.google.gson.annotations.JsonAdapter
import io.glitchlib.model.SubscriptionType
import io.glitchlib.model.SubscriptionTypeAdapter

data class Subscribe(
        val broadcasterId: Long,
        val broadcasterName: String,
        val isGift: Boolean,
        @JsonAdapter(SubscriptionTypeAdapter::class)
        val tier: SubscriptionType,
        val planName: String,
        val userId: Long,
        val userName: String
)