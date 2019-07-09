package io.glitchlib.v5.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.CreatedAt

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface Follow<T> : CreatedAt {
    @get:SerializedName("user", alternate = ["channel"])
    val `data`: T
    @get:SerializedName("notifications")
    val hasNotifications: Boolean
}