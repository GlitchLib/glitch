package glitch.core.api.json.converters;

import com.google.gson.*;
import glitch.core.api.json.enums.UserType;
import java.lang.reflect.Type;

public class UserTypeAdapter implements JsonDeserializer<UserType>, JsonSerializer<UserType> {
    @Override
    public UserType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return UserType.from(json.getAsString());
    }

    @Override
    public JsonElement serialize(UserType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase());
    }
}
