package io.glitchlib.api.model

import com.google.gson.annotations.JsonAdapter
import io.glitchlib.model.CreatedAt
import io.glitchlib.model.IDObject
import io.glitchlib.model.PublishedAt
import io.glitchlib.model.VideoType
import io.glitchlib.model.VideoTypeAdapter
import io.glitchlib.model.VideoViewTypeAdapter
import io.glitchlib.model.ViewType
import java.util.Date
import java.util.Locale

data class Video(
    override val id: Long,
    val userId: Long,
    val userName: String,
    val title: String,
    val description: String,
    override val createdAt: Date,
    override val publishedAt: Date,
    val url: String,
    val thumbnailUrl: String,
    @JsonAdapter(VideoViewTypeAdapter::class)
    val viewable: ViewType,
    val viewCount: Int,
    val language: Locale,
    @JsonAdapter(VideoTypeAdapter::class)
    val type: VideoType
) : IDObject<Long>, CreatedAt, PublishedAt