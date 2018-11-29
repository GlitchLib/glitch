package glitch.kraken.object.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.kraken.object.json.Video;

import java.io.IOException;

public class VideoViewTypeAdapter extends TypeAdapter<Video.ViewType> {
    @Override
    public void write(JsonWriter out, Video.ViewType value) throws IOException {
        out.value(value.name().toLowerCase());
    }

    @Override
    public Video.ViewType read(JsonReader in) throws IOException {
        return Video.ViewType.valueOf(in.nextString().toUpperCase());
    }
}
