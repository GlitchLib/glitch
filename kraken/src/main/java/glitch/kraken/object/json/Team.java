package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.Creation;
import glitch.api.objects.json.interfaces.IDObject;
import glitch.api.objects.json.interfaces.Updated;
import lombok.Data;

import java.time.Instant;

@Data
public class Team implements IDObject<Long>, Creation, Updated {
    @SerializedName("_id")
    private final Long id;
    private final String background;
    private final String banner;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String displayName;
    @SerializedName("info")
    private final String description;
    private final String logo;
    private final String name;
}
