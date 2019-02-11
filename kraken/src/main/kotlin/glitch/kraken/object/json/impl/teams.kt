package glitch.kraken.`object`.json.impl

import com.google.gson.annotations.SerializedName
import glitch.kraken.`object`.json.Team
import glitch.kraken.`object`.json.TeamUsers
import glitch.kraken.`object`.json.User
import java.time.Instant

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class TeamImpl(
        override val id: Long,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        override val background: String,
        override val banner: String,
        override val displayName: String,
        @SerializedName("info")
        override val description: String,
        override val logo: String,
        override val name: String
) : Team

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class TeamUsersImpl(
        override val id: Long,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        override val background: String,
        override val banner: String,
        override val displayName: String,
        @SerializedName("info")
        override val description: String,
        override val logo: String,
        override val name: String,
        override val users: List<User>
) : TeamUsers