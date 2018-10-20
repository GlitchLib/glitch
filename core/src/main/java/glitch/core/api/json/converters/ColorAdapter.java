package glitch.core.api.json.converters;

import com.google.gson.*;
import java.awt.Color;
import java.lang.reflect.Type;

public class ColorAdapter implements JsonDeserializer<Color>, JsonSerializer<Color> {
    @Override
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String value = json.getAsString();
        return (value.matches("^#[0-9A-Fa-f]{1,6}$")) ? Color.decode(value) : (Color) context.deserialize(json, typeOfT);
    }

    @Override
    public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(String.format("#%02x%02x%02x", src.getRed(), src.getGreen(), src.getBlue()));
    }
}
