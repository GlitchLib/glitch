package glitch.pubsub.object.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.pubsub.events.json.VideoPlayback;
import java.io.IOException;

public class VideoPlaybackTypeAdapter extends TypeAdapter<VideoPlayback.Type> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(JsonWriter out, VideoPlayback.Type value) throws IOException {
        out.value(value.name().toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VideoPlayback.Type read(JsonReader in) throws IOException {
        String type = in.nextString().replace("-", "_");
        if (type.equalsIgnoreCase("viewcount")) {
            return VideoPlayback.Type.VIEW_COUNT;
        } else {
            return VideoPlayback.Type.valueOf(type.toUpperCase());
        }
    }
}
