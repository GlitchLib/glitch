package io.glitchlib.model

import com.google.gson.annotations.SerializedName

interface OrdinalList<T> {
    @get:SerializedName(
        value = "data",
        alternate = ["rooms", "users", "follows", "teams", "subscriptions", "videos", "communities", "emoticons",
            "clips", "top", "ingests", "channels", "games", "streams", "teams", "vods", "featured", "results"]
    )
    val data: List<T>
    @get:SerializedName("size", alternate = ["total", "_total"])
    val size: Int
        get() = data.size
}

interface CursorList<T> : OrdinalList<T> {
    @get:SerializedName("cursor", alternate = ["_cursor", "pagination.cursor"])
    val cursor: String?
}
