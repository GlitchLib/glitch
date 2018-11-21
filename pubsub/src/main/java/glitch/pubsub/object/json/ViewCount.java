package glitch.pubsub.object.json;

import lombok.Data;

import java.time.Instant;

@Data
public class ViewCount {
    private final Instant serverTime;
    private final long viewers;
}
