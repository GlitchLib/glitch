package io.glitchlib.pubsub.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.SubscriptionType
import io.glitchlib.pubsub.model.MessageType
import io.glitchlib.pubsub.model.SubscriptionContext
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface Subscription {
    @get:SerializedName("user_name")
    val username: String
    val displayName: String
    val channelName: String
    val userId: Long
    val channelId: Long
    val time: Date
    @get:SerializedName("sub_plan")
    val subscriptionType: SubscriptionType
    @get:SerializedName("sub_plan_name")
    val subscriptionName: String?
    val months: Int
    val context: SubscriptionContext
    @get:SerializedName("sub_message")
    val message: OrdinalMessage?
}

interface IModerator {
    val moderatorName: String
    val moderatorId: Long
}

interface ITarget {
    val targetName: String
    val targetId: Long
}

interface DataType {
    val type: MessageType
}