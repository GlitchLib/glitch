package glitch.api.objects.json.interfaces

import com.google.gson.annotations.SerializedName

interface OrdinalList<E> : Collection<E> {
    @get:SerializedName(value = "data", alternate = ["rooms", "users", "follows", "teams", "subscriptions", "videos", "communities", "emoticons", "clips", "top", "ingests", "channels", "games", "streams", "teams", "vods", "featured", "results"])
    val data: List<E>

    override val size: Int
        get() = data.size

    override fun contains(element: E) = data.contains(element)

    override fun containsAll(elements: Collection<E>) = data.containsAll(elements)

    override fun isEmpty() = data.isEmpty()

    override fun iterator() = data.iterator()
}
