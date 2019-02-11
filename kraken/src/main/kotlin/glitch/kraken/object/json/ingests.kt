package glitch.kraken.`object`.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.IDObject
import glitch.api.objects.json.interfaces.OrdinalList

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
) : Collection<Ingest>, OrdinalList<Ingest> {
    override fun contains(element: Ingest) =
            data.contains(element)

    override fun containsAll(elements: Collection<Ingest>) =
            data.containsAll(elements)

    override fun isEmpty() = data.isEmpty()

    override fun iterator() = data.iterator()

    override val size: Int
        get() = data.size
}