package io.glitchlib.v5.model.json

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import io.glitchlib.model.CreatedAt
import io.glitchlib.model.IDObject
import io.glitchlib.model.SubscriptionType
import io.glitchlib.model.SubscriptionTypeAdapter
import java.util.Date

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Subscriber(
    override val id: String,
    override val createdAt: Date,
    @SerializedName("sub_plan")
    @JsonAdapter(SubscriptionTypeAdapter::class)
    val subscriptionType: SubscriptionType,
    @SerializedName("sub_plan_name")
    val subscriptionName: String,
    val user: User
) : IDObject<String>, CreatedAt
