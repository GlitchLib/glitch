package glitch.helix.`object`.json

import com.google.gson.annotations.JsonAdapter
import glitch.api.objects.adapters.VideoTypeAdapter
import glitch.api.objects.adapters.VideoViewTypeAdapter
import glitch.api.objects.enums.VideoType
import glitch.api.objects.enums.ViewType
import glitch.api.objects.json.interfaces.Creation
import glitch.api.objects.json.interfaces.IDObject
import java.time.Instant
import java.util.Locale

data class Video(
        override val id: Long,
        val userId: Long,
        val userName: String,
        val title: String,
        val description: String,
        override val createdAt: Instant,
        val publishedAt: Instant,
        val url: String,
        val thumbnailUrl: String,
        @JsonAdapter(VideoViewTypeAdapter::class)
        val viewable: ViewType,
        val viewCount: Int,
        val language: Locale,
        @JsonAdapter(VideoTypeAdapter::class)
        val type: VideoType
) : IDObject<Long>, Creation