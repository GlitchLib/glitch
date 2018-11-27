package glitch.kraken.object.json;

import lombok.Data;

import java.time.Instant;

@Data
public class Follow {
    private final Instant createdAt;
    private final boolean notifications;
    private final User user;
}
