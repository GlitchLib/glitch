package glitch.api.objects.json.interfaces

import com.google.gson.annotations.SerializedName

import java.io.Serializable

interface IDObject<ID : Serializable> {
    @get:SerializedName(value = "id", alternate = ["_id"])
    val id: ID
}
