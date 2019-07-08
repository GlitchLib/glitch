package io.glitchlib.v5.model.json

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import io.glitchlib.v5.model.GameNameSerializer
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class ChannelBody internal constructor() {
    @SerializedName("status")
    var title: String? = null
    @JsonAdapter(GameNameSerializer::class)
    var game: Game? = null
    var delay: Int? = null
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class VideoBody internal constructor() {
    val description: String? = null
    @JsonAdapter(GameNameSerializer::class)
    val game: Game? = null
    val language: Locale? = null
    @SerializedName("tag_list")
    val tags: Set<String>? = null
    val title: String? = null
}