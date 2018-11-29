package glitch.kraken.object.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class VideoIdAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter out, Long value) throws IOException {
        out.value("v" + value.toString());
    }

    @Override
    public Long read(JsonReader in) throws IOException {
        return Long.parseLong(in.nextString().substring(1));
    }
}
