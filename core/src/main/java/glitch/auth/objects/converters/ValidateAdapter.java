package glitch.auth.objects.converters;

import com.google.gson.*;
import glitch.auth.objects.Validate;
import glitch.auth.objects.impl.ValidateImpl;

import java.lang.reflect.Type;

public final class ValidateAdapter implements JsonDeserializer<Validate>, JsonSerializer<Validate> {
    @Override
    public Validate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, ValidateImpl.class);
    }

    @Override
    public JsonElement serialize(Validate src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src);
    }
}
