package glitch.core.api.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import glitch.core.api.json.enums.UserType;
import java.io.IOException;

public class UserTypeDeserializer extends JsonDeserializer<UserType> {
    @Override
    public UserType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return UserType.from(p.getValueAsString());
    }
}
