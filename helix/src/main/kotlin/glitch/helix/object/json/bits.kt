package glitch.helix.`object`.json

import com.google.gson.annotations.SerializedName

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Bits(
        val userId: Long,
        @SerializedName("user_name")
        val username: String,
        val rank: Int,
        val score: Int
)