package glitch.core.api.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.awt.Color;
import java.io.IOException;

public class ColorDeserializer extends JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        return (value.matches("^#[0-9A-Fa-f]{1,6}$")) ? Color.decode(value) : ctxt.readValue(p, Color.class);
    }
}
