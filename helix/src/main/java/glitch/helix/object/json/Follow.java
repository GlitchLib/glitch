package glitch.helix.object.json;

import lombok.Data;

import java.time.Instant;

@Data
public class Follow {
    private final Long fromId;
    private final Long toId;
    private final String fromName;
    private final String toName;
    private final Instant followedAt;
}
