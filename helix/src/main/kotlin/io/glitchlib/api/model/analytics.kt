package io.glitchlib.api.model

import com.google.gson.annotations.SerializedName

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ExtensionReport(
    val extensionId: String,
    @SerializedName("URL")
    val url: String,
    val type: String,
    val dateRange: DateRange
)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class GameReport(
    val gameId: Long,
    @SerializedName("URL")
    val url: String,
    val type: String,
    val dateRange: DateRange
)