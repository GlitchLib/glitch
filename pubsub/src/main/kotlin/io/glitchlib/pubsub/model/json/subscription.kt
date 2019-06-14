package io.glitchlib.pubsub.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.SubscriptionType
import io.glitchlib.pubsub.model.SubscriptionContext
import java.util.Date

data class GiftSubscriptionMessage(
    @SerializedName("user_name")
    override val username: String,
    override val displayName: String,
    override val channelName: String,
    override val userId: Long,
    override val channelId: Long,
    override val time: Date,
    @SerializedName("sub_plan")
    override val subscriptionType: SubscriptionType,
    @SerializedName("sub_plan_name")
    override val subscriptionName: String?,
    override val months: Int,
    override val context: SubscriptionContext,
    @SerializedName("sub_message")
    override val message: OrdinalMessage?,
    @SerializedName("recipient_user_name")
    val recipientUsername: String,
    val recipientDisplayName: String
) : Subscription

data class SubscriptionMessage(
    @SerializedName("user_name")
    override val username: String,
    override val displayName: String,
    override val channelName: String,
    override val userId: Long,
    override val channelId: Long,
    override val time: Date,
    @SerializedName("sub_plan")
    override val subscriptionType: SubscriptionType,
    @SerializedName("sub_plan_name")
    override val subscriptionName: String?,
    override val months: Int,
    override val context: SubscriptionContext,
    @SerializedName("sub_message")
    override val message: OrdinalMessage?
) : Subscription