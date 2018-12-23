package glitch.pubsub.object.json;

import lombok.Data;

@Data
public class MessageDelete {
    private final String id;
    private final String moderatorName;
    private final Long moderatorId;
    private final String message;
    private final String targetName;
    private final Long targetId;
}
