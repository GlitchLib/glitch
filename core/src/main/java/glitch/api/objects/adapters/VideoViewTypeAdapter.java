package glitch.api.objects.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.api.objects.enums.ViewType;

import java.io.IOException;

public class VideoViewTypeAdapter extends TypeAdapter<ViewType> {
    @Override
    public void write(JsonWriter out, ViewType value) throws IOException {
        out.value(value.name().toLowerCase());
    }

    @Override
    public ViewType read(JsonReader in) throws IOException {
        return ViewType.valueOf(in.nextString().toUpperCase());
    }
}
