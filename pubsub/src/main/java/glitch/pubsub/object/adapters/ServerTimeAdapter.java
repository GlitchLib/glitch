package glitch.pubsub.object.adapters;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Instant;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class ServerTimeAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String[] secdons = json.getAsString().split("\\.");
        long s = Long.parseLong(secdons[0]);
        long ns = Long.parseLong(secdons[1]) * 100;
        return Instant.ofEpochSecond(s, ns);
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getNano());
    }
}
