package glitch.kraken.`object`.json

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import glitch.api.objects.adapters.VideoTypeAdapter
import glitch.api.objects.adapters.VideoViewTypeAdapter
import glitch.api.objects.enums.VideoType
import glitch.api.objects.enums.ViewType
import glitch.api.objects.json.interfaces.Creation
import glitch.api.objects.json.interfaces.IDObject
import glitch.kraken.`object`.adapters.VideoIdAdapter
import glitch.kraken.`object`.adapters.VideoStatusAdapter
import java.time.Instant
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
        override val createdAt: Instant,

        val description: String,
        val descriptionHtml: String,
        val fps: Map<String, Double>,

        val game: String,
        val language: Locale,
        val preview: Image,
        val publishedAt: Instant,
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
        val viewableAt: Instant,
        val views: Int
) : IDObject<Long>, Creation {

    data class Thumbnails(
            val large: Thumbnail,
            val medium: Thumbnail,
            val small: Thumbnail,
            val template: Thumbnail
    ) {
        fun getCustomSize(width: Int, height: Int): Thumbnail {
            return Thumbnail(template.type, template.url.replace("{width}", Integer.toString(width))
                    .replace("{height}", Integer.toString(height)))
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
