package glitch.pubsub.object.json;

import glitch.pubsub.object.json.message.Emote;
import lombok.Data;

import java.util.List;

@Data
public class OrdinalMessage {
    private final String message;
    private final List<Emote> emotes;
}
