package io.glitchlib.v5.model.json

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.IDObject
import io.glitchlib.model.OrdinalList

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Ingest(
        @SerializedName("_id")
        override val id: Int,
        val availability: Double,
        val isDefault: Boolean,
        val name: String,
        val urlTemplate: String
) : IDObject<Int> {
    fun buildUrl(streamKey: String): String {
        return urlTemplate.replace("{stream_key}", streamKey)
    }
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class IngestCollection(
        override val data: List<Ingest>
) : OrdinalList<Ingest> {
    override val size: Int
        get() = data.size
}