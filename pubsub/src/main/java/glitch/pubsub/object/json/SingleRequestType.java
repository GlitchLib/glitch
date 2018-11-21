package glitch.pubsub.object.json;

import glitch.pubsub.object.enums.MessageType;
import lombok.Data;

@Data
public class SingleRequestType {
    private final MessageType type;
}
