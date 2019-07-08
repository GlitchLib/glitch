package io.glitchlib.v5.internal.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.model.json.Cheermote
import io.glitchlib.v5.model.json.User

data class Cheermotes(
        @SerializedName("actions")
        override val data: List<Cheermote>
) : OrdinalList<Cheermote>

data class Editors(
        @SerializedName("users")
        override val data: List<User>
) : OrdinalList<User>