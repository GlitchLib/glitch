package glitch.kraken.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.adapters.SerializeTo
import glitch.api.objects.json.interfaces.Creation
import glitch.api.objects.json.interfaces.IDObject
import glitch.api.objects.json.interfaces.Updated
import glitch.kraken.`object`.json.impl.TeamImpl
import glitch.kraken.`object`.json.impl.TeamUsersImpl

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SerializeTo(TeamImpl::class)
interface Team : IDObject<Long>, Creation, Updated {
    val background: String
    val banner: String
    val displayName: String
    @get:SerializedName("info")
    val description: String
    val logo: String
    val name: String
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SerializeTo(TeamUsersImpl::class)
interface TeamUsers : Team {
    val users: List<User>
}