package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.Creation;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import java.time.Instant;

@Data
public class Stream implements IDObject<Long>, Creation {
    private final Long id;
    private final String game;
    private final int viewers;
    private final int videoHeight;
    private final int averageFps;
    private final int delay;
    private final Instant createdAt;
    @SerializedName("is_playlist")
    private final boolean playlist;
    private final Image preview;
    private final Channel channel;
}
