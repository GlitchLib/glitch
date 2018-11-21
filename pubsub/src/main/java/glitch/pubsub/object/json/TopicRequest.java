package glitch.pubsub.object.json;

import com.google.gson.JsonObject;
import glitch.pubsub.object.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TopicRequest extends SingleRequestType {
    private final String nonce;
    private final JsonObject data;

    public TopicRequest(MessageType type, String nonce, JsonObject data) {
        super(type);
        this.nonce = nonce;
        this.data = data;
    }
}
