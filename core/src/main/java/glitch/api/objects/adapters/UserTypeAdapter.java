package glitch.api.objects.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.api.objects.enums.UserType;

import java.io.IOException;

public class UserTypeAdapter extends TypeAdapter<UserType> {
    @Override
    public void write(JsonWriter out, UserType value) throws IOException {
        out.value(value.name().toLowerCase());
    }

    @Override
    public UserType read(JsonReader in) throws IOException {
        return UserType.from(in.nextString());
    }
}
