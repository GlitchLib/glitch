package glitch.helix.object.json;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.IDObject;
import glitch.helix.object.adapters.StreamTypeSerializer;
import lombok.Data;

import java.time.Instant;
import java.util.Locale;

@Data
public class Stream implements IDObject<Long> {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final Long gameId;
    private final String[] communityIds;
    @SerializedName("type")
    @JsonAdapter(StreamTypeSerializer.class)
    private final boolean live;
    private final String title;
    private final int viewerCount;
    private final Instant startedAt;
    private final Locale language;
    private final String thumbnailUrl;
}
