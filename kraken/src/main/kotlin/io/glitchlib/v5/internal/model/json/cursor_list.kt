package io.glitchlib.v5.internal.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.CursorList
import io.glitchlib.v5.model.json.ChannelFollow

data class ChannelFollows(
        override val data: List<ChannelFollow>,
        @SerializedName("_cursor")
        override val cursor: String,
        @SerializedName("_total")
        override val size: Int
) : CursorList<ChannelFollow>