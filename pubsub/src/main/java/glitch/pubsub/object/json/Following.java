package glitch.pubsub.object.json;

import lombok.Data;

@Data
public class Following {
    private final String username;
    private final String displayName;
    private final Long userId;
}
