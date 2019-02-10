package glitch.pubsub.object.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.pubsub.object.json.ModerationData;
import java.io.IOException;

public class ModerationActionAdapter extends TypeAdapter<ModerationData.Action> {
    @Override
    public void write(JsonWriter out, ModerationData.Action value) throws IOException {
        out.value(value.name().toLowerCase());
    }

    @Override
    public ModerationData.Action read(JsonReader in) throws IOException {
        return ModerationData.Action.valueOf(in.nextString().toUpperCase());
    }
}
