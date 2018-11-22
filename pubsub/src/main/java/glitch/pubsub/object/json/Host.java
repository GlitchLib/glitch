package glitch.pubsub.object.json;

import lombok.Data;

@Data
public class Host {
    private final String moderatorName;
    private final Long moderatorId;
    private final String targetName;
    private final Long targetId;
}