package io.glitchlib.v5.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.CreatedAt
import io.glitchlib.model.IDObject
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Clip(
        @SerializedName("slug")
        override val id: String,
        val trackingId: Long,
        val url: String,
        val embedUrl: String,
        val embedHtml: String,
        val broadcaster: ClipUser,
        val curator: ClipUser,
        val vod: VOD,
        val game: String,
        val language: Locale,
        val title: String,
        val views: Int,
        val duration: Float,
        override val createdAt: Date,
        val thumbnalis: Thumbnalis
) : IDObject<String>, CreatedAt {

    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    data class ClipUser(
            override val id: Long,
            @SerializedName("name")
            val username: String,
            val displayName: String,
            val channelUrl: String,
            val logo: String
    ) : IDObject<Long>

    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    data class VOD(
            override val id: Long,
            val url: String
    ) : IDObject<Long>

    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    data class Thumbnalis(
            val medium: String,
            val small: String,
            val tiny: String
    )
}