package glitch.auth.json.converters;

import com.google.gson.*;
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
