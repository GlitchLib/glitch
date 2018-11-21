package glitch.kraken.object.json;

import glitch.api.objects.json.interfaces.Creation;
import lombok.Data;

import java.time.Instant;

@Data
public class UserFollow implements Creation {
    private final Instant createdAt;
    private final boolean notifications;
    private final Channel channel;
}
