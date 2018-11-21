package glitch.pubsub.object.json;

import com.google.gson.annotations.JsonAdapter;
import glitch.pubsub.exceptions.PubSubException;
import glitch.pubsub.object.adapters.ExceptionAdapter;
import glitch.pubsub.object.adapters.MessageTypeAdapter;
import glitch.pubsub.object.enums.MessageType;
import lombok.Data;

@Data
public class Response {
    @JsonAdapter(MessageTypeAdapter.class)
    private final MessageType type;
    private final String nonce;
    @JsonAdapter(ExceptionAdapter.class)
    private final PubSubException error;
    private final ResponseData data;
}
