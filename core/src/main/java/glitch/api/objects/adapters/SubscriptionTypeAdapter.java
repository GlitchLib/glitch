package glitch.api.objects.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.api.objects.enums.SubscriptionType;
import java.io.IOException;

public class SubscriptionTypeAdapter extends TypeAdapter<SubscriptionType> {

    @Override
    public void write(JsonWriter out, SubscriptionType value) throws IOException {
        out.value((value.getValue().equals("")) ? null : value.getValue());
    }

    @Override
    public SubscriptionType read(JsonReader in) throws IOException {
        return SubscriptionType.from(in.nextString());
    }
}
