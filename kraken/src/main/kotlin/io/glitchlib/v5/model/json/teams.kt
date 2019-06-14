package io.glitchlib.v5.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.CreatedAndUpdatedAt
import io.glitchlib.model.IDObject
import io.glitchlib.model.SerializeTo
import io.glitchlib.v5.internal.model.json.TeamImpl
import io.glitchlib.v5.internal.model.json.TeamItemImpl

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@SerializeTo(TeamItemImpl::class)
interface TeamItem : IDObject<Long>, CreatedAndUpdatedAt {
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
@SerializeTo(TeamImpl::class)
interface Team : TeamItem {
    val users: List<User>
}