package glitch.pubsub.object.json;

import com.google.gson.JsonObject;
import glitch.pubsub.Topic;
import lombok.Data;

@Data
public class ResponseData {
    private final Topic topic;
    private final JsonObject message;
}
