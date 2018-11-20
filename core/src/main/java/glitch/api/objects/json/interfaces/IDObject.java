package glitch.api.objects.json.interfaces;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public interface IDObject<ID extends Serializable> {
    @SerializedName(value = "id", alternate = "_id")
    ID getId();
}
