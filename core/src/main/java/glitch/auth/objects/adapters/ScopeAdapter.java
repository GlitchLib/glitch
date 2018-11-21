package glitch.auth.objects.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.auth.Scope;

import java.io.IOException;

public final class ScopeAdapter extends TypeAdapter<Scope> {
    @Override
    public void write(JsonWriter out, Scope value) throws IOException {
        out.value(value.getValue());
    }

    @Override
    public Scope read(JsonReader in) throws IOException {
        return Scope.from(in.nextString());
    }
}
