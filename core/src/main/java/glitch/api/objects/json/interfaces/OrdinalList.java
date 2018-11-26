package glitch.api.objects.json.interfaces;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;

public interface OrdinalList<E> {
    @SerializedName(value = "data", alternate = {
            "rooms"
    })
    ImmutableList<E> getData();

    @SerializedName("_total")
    int size();
}
