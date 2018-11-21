package glitch.pubsub.object.json;

import com.google.gson.annotations.JsonAdapter;
import glitch.pubsub.object.adapters.VideoPlaybackTypeAdapter;
import lombok.Data;

import java.time.Instant;

@Data
public class VideoPlayback {
    @JsonAdapter(VideoPlaybackTypeAdapter.class)
    private final Type type;
    private final Instant serverTime;

    public enum Type {
        STREAM_UP,
        STREAM_DOWN,
        VIEW_COUNT
    }
}
