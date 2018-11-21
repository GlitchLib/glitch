package glitch.pubsub.object.json;

import lombok.Data;

@Data
public class ModeratorActivation {
    private final String moderatorName;
    private final Long moderatorId;
    private final boolean active;
}
