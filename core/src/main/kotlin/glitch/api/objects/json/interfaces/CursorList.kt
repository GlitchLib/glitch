package glitch.api.objects.json.interfaces

import com.google.gson.annotations.SerializedName

interface CursorList<E> : OrdinalList<E> {
    @get:SerializedName(value = "cursor", alternate = ["_cursor"])
    val cursor: String?
}
