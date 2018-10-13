package glitch.core.api.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public interface OrdinalList<E extends IDObject<?>> extends List<E> {
    @SerializedName(value = "data", alternate = {
            "rooms"
    })
    ImmutableList<E> getData();

    @Override
    @SerializedName("_total")
    int size();
}
