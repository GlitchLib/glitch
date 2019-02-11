package glitch.kraken.object.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import glitch.kraken.object.json.Game;
import java.lang.reflect.Type;

public class GameNameSerializer implements JsonSerializer<Game> {
    @Override
    public JsonElement serialize(Game src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }
}
