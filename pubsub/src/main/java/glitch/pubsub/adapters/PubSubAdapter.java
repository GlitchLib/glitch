package glitch.pubsub.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import glitch.pubsub.GlitchPubSub;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PubSubAdapter implements JsonDeserializer<GlitchPubSub> {
    private final GlitchPubSub pubSub;

    @Override
    public GlitchPubSub deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return pubSub;
    }
}
