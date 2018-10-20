package glitch.core.api.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;

public interface OrdinalList<E extends IDObject<?>> {
    @SerializedName(value = "data", alternate = {
            "rooms"
    })
    ImmutableList<E> getData();

    @SerializedName("_total")
    int size();
}
