package glitch.helix.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.CursorList
import glitch.api.objects.json.interfaces.OrdinalList

// Analytics

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ExtensionAnalytics(
        override val data: List<ExtensionReport>
) : OrdinalList<ExtensionReport>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class GameAnalytics(
        override val data: List<GameData>
) : OrdinalList<GameData>

// Bits

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class BitsLeaderboard(
        override val data: List<Bits>,
        @SerializedName("total")
        override val size: Int,
        val dateRange: DateRange
) : OrdinalList<Bits>

// Clips

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class SingleClipCreate(
        override val data: List<ClipCreate>
) : OrdinalList<ClipCreate>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Clips(
        override val data: List<Clip>,
        @SerializedName("pagination.cursor")
        override val cursor: String?
) : CursorList<Clip>

// Entitlements

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UploadEntitlement(
        override val data: List<UploadData>
) : OrdinalList<UploadData>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Codes(
        @SerializedName("results")
        override val data: List<Code>
) : OrdinalList<Code>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Games(
        override val data: List<Game>,
        @SerializedName("pagination.cursor")
        override val cursor: String
) : CursorList<Game>


/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Users(
        override val data: List<User>
) : OrdinalList<User>

data class Extensions(
        override val data: List<Extension>
) : OrdinalList<Extension>

data class Follows(
        override val data: List<Follow>,
        @SerializedName("total")
        override val size: Int,
        @field:SerializedName("pagination.cursor")
        override val cursor: String
) : CursorList<Follow>

data class InstalledExtensions(
        override val data: List<InstalledExtension>
) : OrdinalList<InstalledExtension>

data class Streams(
        override val data: List<Stream>,
        @SerializedName("pagination.cursor")
        override val cursor: String
) : CursorList<Stream>

data class Videos(
        override val data: List<Video>,
        @SerializedName("pagination.cursor")
        override val cursor: String
) : CursorList<Video>