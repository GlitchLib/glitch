package glitch.kraken.object.json;

import lombok.Data;

import java.time.Instant;

@Data
public class UserFollow {
    private final Instant createdAt;
    private final boolean notifications;
    private final User user;
}
