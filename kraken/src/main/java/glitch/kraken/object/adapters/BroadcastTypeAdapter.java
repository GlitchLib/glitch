package glitch.kraken.object.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.kraken.object.json.Video;

import java.io.IOException;

public class BroadcastTypeAdapter extends TypeAdapter<Video.Type> {
    @Override
    public void write(JsonWriter out, Video.Type value) throws IOException {
        out.value(value.name().toLowerCase());
    }

    @Override
    public Video.Type read(JsonReader in) throws IOException {
        return Video.Type.valueOf(in.nextString().toUpperCase());
    }
}
