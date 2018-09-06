package glitch.common.json.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import glitch.auth.Scope;
import java.io.IOException;

public class ScopeDeserializer extends JsonDeserializer<Scope> {
    @Override
    public Scope deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Scope.from(p.getValueAsString());
    }
}
