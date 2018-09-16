package glitch.auth.json.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Calendar;

public final class ExpireInstantDeserializer implements JsonDeserializer<Calendar>, JsonSerializer<Calendar> {
    @Override
    public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, json.getAsInt());
        return calendar;
    }

    @Override
    public JsonElement serialize(Calendar src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTime().toString());
    }
}
