package glitch.core.api.json.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import glitch.auth.Scope;
import java.lang.reflect.Type;

public class ScopeAdapter implements JsonDeserializer<Scope>, JsonSerializer<Scope> {
    @Override
    public Scope deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Scope.from(json.getAsString());
    }

    @Override
    public JsonElement serialize(Scope src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getValue());
    }
}
