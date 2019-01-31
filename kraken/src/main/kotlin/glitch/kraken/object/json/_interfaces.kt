package glitch.kraken.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.Creation

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface Follow<T> : Creation {
    @get:SerializedName("user", alternate = ["channel"])
    val `data` : T
    @get:SerializedName("notifications")
    val hasNotifications: Boolean
}