package glitch.pubsub.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.enums.SubscriptionType
import glitch.pubsub.`object`.enums.SubscriptionContext
import java.time.Instant

open class SubscriptionMessage(
        @SerializedName("user_name")
        val username: String, val displayName: String,
        val channelName: String, val userId: Long,
        val channelId: Long, val time: Instant,
        @SerializedName("sub_plan")
        val subscriptionType: SubscriptionType,
        @SerializedName("sub_plan_name")
        val subscriptionName: String?,
        val months: Int, val context: SubscriptionContext,
        @SerializedName("sub_message")
        val message: OrdinalMessage?) {

}
