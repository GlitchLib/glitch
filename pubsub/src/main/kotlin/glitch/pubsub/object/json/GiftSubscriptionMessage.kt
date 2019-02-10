package glitch.pubsub.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.enums.SubscriptionType
import glitch.pubsub.`object`.enums.SubscriptionContext
import java.time.Instant

class GiftSubscriptionMessage(
        username: String, displayName: String,
        channelName: String, userId: Long,
        channelId: Long, time: Instant,
        subscriptionType: SubscriptionType, subscriptionName: String,
        months: Int, context: SubscriptionContext,
        message: OrdinalMessage, val recipientId: Long,
        @SerializedName("recipient_user_name")
        val recipientUsername: String, val recipientDisplayName: String
) : SubscriptionMessage(username, displayName, channelName, userId, channelId, time, subscriptionType, subscriptionName, months, context, message)
