package glitch.api.objects.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.api.objects.enums.VideoType;
import java.io.IOException;

public class VideoTypeAdapter extends TypeAdapter<VideoType> {
    @Override
    public void write(JsonWriter out, VideoType value) throws IOException {
        out.value(value.name().toLowerCase());
    }

    @Override
    public VideoType read(JsonReader in) throws IOException {
        return VideoType.valueOf(in.nextString().toUpperCase());
    }
}
