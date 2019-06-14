package io.glitchlib.model

import com.google.gson.annotations.SerializedName
import io.glitchlib.GlitchClient
import io.glitchlib.internal.http.HttpStatus
import java.io.Serializable
import java.util.Date
import java.util.UUID

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface GlitchObject {
    val client: GlitchClient
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface IDObject<out S : Serializable> {
    @get:SerializedName("id", alternate = ["_id"])
    val id: S
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface CreatedAt {
    val createdAt: Date
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface UpdatedAt {
    val updatedAt: Date
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface CreatedAndUpdatedAt : CreatedAt, UpdatedAt

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
interface PublishedAt {
    val publishedAt: Date
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ErrorResponse(
    override val client: GlitchClient,
    val error: String,
    val status: Int,
    val message: String?
) : GlitchObject {
    fun toStatus() = HttpStatus(status, if (message.isNullOrEmpty()) error else message)
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Badge(
    override val id: String,
    val version: Int
) : IDObject<String>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChatRoom(
    override val id: UUID,
    val ownerId: Long,
    val name: String,
    val topic: String,
    val isPreviewable: Boolean,
    val minimumAllowedRole: Role
) : IDObject<UUID> {

    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    enum class Role {
        EVERYONE,
        SUBSCRIBER,
        MODERATOR
    }
}