package io.glitchlib.v5.internal.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.v5.model.json.Team
import io.glitchlib.v5.model.json.TeamItem
import io.glitchlib.v5.model.json.User
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class TeamItemImpl(
        override val id: Long,
        override val createdAt: Date,
        override val updatedAt: Date,
        override val background: String,
        override val banner: String,
        override val displayName: String,
        @SerializedName("info")
        override val description: String,
        override val logo: String,
        override val name: String
) : TeamItem

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class TeamImpl(
        override val id: Long,
        override val createdAt: Date,
        override val updatedAt: Date,
        override val background: String,
        override val banner: String,
        override val displayName: String,
        @SerializedName("info")
        override val description: String,
        override val logo: String,
        override val name: String,
        override val users: List<User>
) : Team