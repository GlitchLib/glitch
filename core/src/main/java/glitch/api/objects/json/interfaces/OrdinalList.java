package glitch.api.objects.json.interfaces;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;

public interface OrdinalList<E> {
    @SerializedName(value = "data", alternate = {
            "rooms",
            "users",
            "follows",
            "teams",
            "subscriptions",
            "videos",
            "communities",
            "emoticons",
            "clips"
    })
    ImmutableList<E> getData();

    @SerializedName("_total")
    default int size() {
        return getData().size();
    }
}
