package glitch.api.objects.json.interfaces;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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
            "clips",
            "top",
            "ingests",
            "channels",
            "games",
            "streams",
            "teams",
            "vods",
            "featured"
    })
    List<E> getData();

    @SerializedName("_total")
    default int size() {
        return getData().size();
    }
}
