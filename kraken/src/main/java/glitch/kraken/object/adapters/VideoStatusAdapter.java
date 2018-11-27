package glitch.kraken.object.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.kraken.object.json.Video;

import java.io.IOException;

public class VideoStatusAdapter extends TypeAdapter<Video.Status> {
    @Override
    public void write(JsonWriter out, Video.Status value) throws IOException {
        out.value(value.name().toLowerCase());
    }

    @Override
    public Video.Status read(JsonReader in) throws IOException {
        return Video.Status.valueOf(in.nextString().toUpperCase());
    }
}
