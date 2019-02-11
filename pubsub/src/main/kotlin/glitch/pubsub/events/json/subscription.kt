package glitch.pubsub.events.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.enums.SubscriptionType
import glitch.pubsub.`object`.enums.SubscriptionContext
import java.time.Instant

data class GiftSubscriptionMessage(
        @SerializedName("user_name")
        override val username: String,
        override val displayName: String,
        override val channelName: String,
        override val userId: Long,
        override val channelId: Long,
        override val time: Instant,
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
        override val time: Instant,
        @SerializedName("sub_plan")
        override val subscriptionType: SubscriptionType,
        @SerializedName("sub_plan_name")
        override val subscriptionName: String?,
        override val months: Int,
        override val context: SubscriptionContext,
        @SerializedName("sub_message")
        override val message: OrdinalMessage?
) : Subscription