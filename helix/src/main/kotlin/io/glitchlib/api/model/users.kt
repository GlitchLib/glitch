package io.glitchlib.api.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import io.glitchlib.model.BroadcasterType
import io.glitchlib.model.IDObject
import io.glitchlib.model.UserType
import io.glitchlib.model.UserTypeAdapter
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class User(
        val broadcasterType: BroadcasterType,
        val description: String,
        val displayName: String,
        override val id: Long,
        @SerializedName("login")
        val username: String,
        val offlineImageUrl: String,
        val profileImageUrl: String,
        @JsonAdapter(UserTypeAdapter::class)
        val type: UserType,
        val viewCount: Long,
        val email: String?
) : IDObject<Long>

data class InstalledExtension(
        override val id: String,
        val version: String,
        val name: String,
        @SerializedName("active")
        val isActive: Boolean
) : IDObject<String>

data class InstalledExtensionComponent(
        override val id: String,
        val version: String,
        val name: String,
        @SerializedName("active")
        val isActive: Boolean,
        val x: Int,
        val y: Int
) : IDObject<String>

data class Extension(
        override val id: String,
        val version: String,
        val name: String,
        val canActivate: Boolean,
        val type: List<ExtensionType>
) : IDObject<String>

data class Follow(
        val fromId: Long,
        val toId: Long,
        val fromName: String,
        val toName: String,
        val followedAt: Date
)

data class UserExtensionComponent(
        val panel: Map<Int, InstalledExtension>,
        val overlay: Map<Int, InstalledExtension>,
        val component: Map<Int, InstalledExtensionComponent>,
        val mobile: Map<Int, InstalledExtension>
)