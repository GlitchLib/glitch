package glitch.auth.objects.converters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class ExpireInstantAdapter implements JsonDeserializer<Instant>, JsonSerializer<Instant> {
    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Instant.now().plus(json.getAsLong(), ChronoUnit.SECONDS);
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
