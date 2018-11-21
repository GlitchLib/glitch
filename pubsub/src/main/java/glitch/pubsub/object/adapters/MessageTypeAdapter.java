package glitch.pubsub.object.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.pubsub.object.enums.MessageType;

import java.io.IOException;

public class MessageTypeAdapter extends TypeAdapter<MessageType> {
    @Override
    public void write(JsonWriter out, MessageType value) throws IOException {
        out.value(value.name());
    }

    @Override
    public MessageType read(JsonReader in) throws IOException {
        return MessageType.valueOf(in.nextString());
    }
}
