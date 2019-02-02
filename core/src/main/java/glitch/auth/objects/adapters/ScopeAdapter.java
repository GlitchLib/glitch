package glitch.auth.objects.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.auth.GlitchScope;
import java.io.IOException;

public final class ScopeAdapter extends TypeAdapter<GlitchScope> {
    @Override
    public void write(JsonWriter out, GlitchScope value) throws IOException {
        out.value(value.getValue());
    }

    @Override
    public GlitchScope read(JsonReader in) throws IOException {
        return GlitchScope.of(in.nextString());
    }
}
