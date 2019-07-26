package io.glitchlib.api.model

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.IDObject
import io.glitchlib.model.OrdinalList
import java.util.Date


/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class BitsLeaderboard(
    override val data: List<BitsRank>,
    @SerializedName("total")
    override val size: Int,
    val dateRange: DateRange
) : OrdinalList<BitsRank>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class BitsRank(
    val userId: Long,
    @SerializedName("user_name")
    val username: String,
    val rank: Int,
    val score: Int
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ExtensionInvoice(
    override val id: String,
    val timestamp: Date,
    val broadcasterId: Long,
    val broadcasterName: String,
    val userId: Long,
    @SerializedName("user_name")
    val username: String,
    val productType: ProductType,
    val productData: ProductData
) : IDObject<String>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ProductData(
    val sku: String,
    val cost: Cost,
    val displayName: String,
    val inDevelopment: Boolean
) {
    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    data class Cost(
        val amount: Int, // TODO: if someone report cannot serialize bigger amount, change to `Long`
        val type: String
    )
}