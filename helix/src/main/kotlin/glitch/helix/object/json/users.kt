package glitch.helix.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.enums.BroadcasterType
import glitch.api.objects.enums.UserType
import glitch.api.objects.json.interfaces.IDObject
import glitch.helix.`object`.enums.ExtensionType
import java.time.Instant

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
        val type: UserType,
        val viewCount: Long,
        val email: String?
) : IDObject<Long>

data class InstalledExtension(
        val id: String,
        val version: String,
        val name: String,
        @SerializedName("active")
        val isActive: Boolean
)

data class Extension(
        val id: String,
        val version: String,
        val name: String,
        val canActivate: Boolean,
        val type: List<ExtensionType>
)

data class Follow(
        val fromId: Long,
        val toId: Long,
        val fromName: String,
        val toName: String,
        val followedAt: Instant
)