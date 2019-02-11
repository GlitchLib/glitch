package glitch.kraken.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.enums.SubscriptionType
import glitch.api.objects.json.interfaces.Creation
import glitch.api.objects.json.interfaces.IDObject
import java.time.Instant

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Subscriber(
        override val id: String,
        override val createdAt: Instant,
        @SerializedName("sub_plan")
        val subscriptionType: SubscriptionType,
        @SerializedName("sub_plan_name")
        val subscriptionName: String,
        val user: User
) : IDObject<String>, Creation
