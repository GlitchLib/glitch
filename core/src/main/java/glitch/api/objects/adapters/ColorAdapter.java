package glitch.api.objects.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.awt.*;
import java.io.IOException;

public class ColorAdapter extends TypeAdapter<Color> {
    @Override
    public void write(JsonWriter out, Color value) throws IOException {
        out.value(String.format("#%02x%02x%02x", value.getRed(), value.getGreen(), value.getBlue()));
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        return Color.decode(in.nextString());
    }
}
