package glitch.pubsub.object.json;

import lombok.Data;

import java.util.List;

@Data
public class ExtensionBroadcastMessage {
    private final List<String> context;
}
