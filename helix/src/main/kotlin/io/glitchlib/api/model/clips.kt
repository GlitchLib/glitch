package io.glitchlib.api.model

import io.glitchlib.model.CreatedAt
import io.glitchlib.model.IDObject
import java.util.Date
import java.util.Locale

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ClipCreate(
    override val id: String,
    val editUrl: String
) : IDObject<String>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Clip(
    override val id: String,
    val url: String,
    val embedUrl: String,
    val broadcasterId: Long,
    val broadcasterName: String,
    val creatorId: Long,
    val creatorName: String,
    val videoId: Long,
    val gameId: Long,
    val language: Locale,
    val title: String,
    val viewCount: Int,
    override val createdAt: Date,
    val thumbnailUrl: String
) : IDObject<String>, CreatedAt