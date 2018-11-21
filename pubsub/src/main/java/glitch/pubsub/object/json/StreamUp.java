package glitch.pubsub.object.json;

import lombok.Data;

import java.time.Instant;

@Data
public class StreamUp {
    private final Instant serverTime;
    private final int delay;
}
