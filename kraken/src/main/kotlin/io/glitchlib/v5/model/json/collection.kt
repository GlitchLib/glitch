package io.glitchlib.v5.model.json

import io.glitchlib.model.CreatedAndUpdatedAt
import io.glitchlib.model.IDObject
import io.glitchlib.model.PublishedAt
import java.util.*

data class Collection(
        override val id: String,
        override val createdAt: Date,
        override val updatedAt: Date,
        val itemsCount: Int,
        val owner: User,
        val thumbnails: Thumbnails,
        val title: String,
        val totalDuration: Long,
        val views: Int
) : IDObject<String>, CreatedAndUpdatedAt {
    data class Thumbnails(
            val small: String,
            val medium: String,
            val large: String,
            val template: String
    )

    data class Item(
            override val id: String,
            override val createdAt: Date,
            override val updatedAt: Date,
            override val publishedAt: Date,
            val descriptionHtml: String,
            val duration: Long,
            val game: String,
            val itemId: String,
            val itemType: String,
            val owner: User,
            val thumbnails: Thumbnails,
            val title: String,
            val views: Int
    ) : IDObject<String>, CreatedAndUpdatedAt, PublishedAt
}