package glitch.pubsub.object.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.pubsub.exceptions.PubSubException;

import java.io.IOException;

public class ExceptionAdapter extends TypeAdapter<PubSubException> {
    @Override
    public void write(JsonWriter out, PubSubException value) throws IOException {
        out.value(value.getLocalizedMessage());
    }

    @Override
    public PubSubException read(JsonReader in) throws IOException {
        String response = in.nextString();

        switch (response) {
            case "ERR_BADMESSAGE":
                response = "Inappropriate message!";
                break;
            case "ERR_BADAUTH":
                response = "Failed to using authorization!";
                break;
            case "ERR_SERVER":
                response = "Internal Server Error!";
                break;
            case "ERR_BADTOPIC":
                response = "Inappropriate topic!";
                break;
        }

        return new PubSubException(response);
    }
}
