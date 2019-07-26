package io.glitchlib.api.model

import java.util.Date


/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class DateRange(val startedAt: Date, val endedAt: Date)

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ManifestBody(
    val campaignId: String,
    val clientId: String,
    val gameWatched: String,
    val broadcasterId: String,
    val itemId: String,
    val reason: DropReason,
    val users: Collection<String>
)