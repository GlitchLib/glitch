package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

@Data
public class EmoteSetData implements IDObject<Long> {
    private final Long id;
    @SerializedName("code")
    private final String name;
}
