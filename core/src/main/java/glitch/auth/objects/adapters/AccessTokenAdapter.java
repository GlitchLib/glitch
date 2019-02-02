package glitch.auth.objects.adapters;

import com.google.gson.*;
import glitch.auth.objects.json.AccessToken;
import glitch.auth.objects.json.impl.AccessTokenImpl;
import java.lang.reflect.Type;

public final class AccessTokenAdapter implements JsonDeserializer<AccessToken>, JsonSerializer<AccessToken> {
    @Override
    public AccessToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, AccessTokenImpl.class);
    }

    @Override
    public JsonElement serialize(AccessToken src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src);
    }
}
