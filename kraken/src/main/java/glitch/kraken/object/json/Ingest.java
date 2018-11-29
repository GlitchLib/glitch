package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class Ingest implements IDObject<Integer> {
    @SerializedName("_id")
    private final Integer id;
    private final double availability;
    @Accessors(fluent = true)
    @SerializedName("default")
    private final boolean isDefault;
    private final String name;
    private final String urlTemplate;

    public String buildUrl(String streamKey) {
        return urlTemplate.replace("{stream_key}", streamKey);
    }
}
