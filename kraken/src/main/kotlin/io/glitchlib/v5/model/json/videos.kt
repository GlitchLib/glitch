package io.glitchlib.v5.model.json

import com.google.gson.JsonObject
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import io.glitchlib.model.CreatedAt
import io.glitchlib.model.IDObject
import io.glitchlib.model.VideoType
import io.glitchlib.model.VideoTypeAdapter
import io.glitchlib.model.VideoViewTypeAdapter
import io.glitchlib.model.ViewType
import io.glitchlib.v5.model.VideoIdAdapter
import io.glitchlib.v5.model.VideoStatusAdapter
import java.util.Date
import java.util.Locale

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Video(
    @JsonAdapter(VideoIdAdapter::class)
    override val id: Long,
    val broadcastId: Long,
    @JsonAdapter(VideoTypeAdapter::class)
    @SerializedName("broadcast_type")
    val broadcastType: VideoType,
    val channel: Channel,
    override val createdAt: Date,

    val description: String,
    val descriptionHtml: String,
    val fps: Map<String, Double>,

    val game: String,
    val language: Locale,
    val preview: Image,
    val publishedAt: Date,
    val resolutions: Map<String, String>,
    @JsonAdapter(VideoStatusAdapter::class)
    val status: Status,
    val tagList: String,
    val thumbnails: Thumbnails,
    val title: String,
    val url: String,
    @SerializedName("viewable")
    @JsonAdapter(VideoViewTypeAdapter::class)
    val viewType: ViewType,
    val viewableAt: Date,
    val views: Int
) : IDObject<Long>, CreatedAt {

    data class Thumbnails(
        val large: Thumbnail,
        val medium: Thumbnail,
        val small: Thumbnail,
        val template: Thumbnail
    ) {
        fun getCustomSize(width: Int, height: Int): Thumbnail {
            return Thumbnail(
                template.type, template.url.replace("{width}", Integer.toString(width))
                    .replace("{height}", Integer.toString(height))
            )
        }

        data class Thumbnail(
            val type: String,
            val url: String
        )
    }

    enum class Status {
        CREATED,
        RECORDING,
        RECORDED
    }
}

data class VideoCreate(
    val upload: UploadData,
    val video: JsonObject // this object is not equal like `Video` data class, so using a `JsonObject` is a safe solution
) {
    data class UploadData(
        val token: String,
        val url: String
    )
}