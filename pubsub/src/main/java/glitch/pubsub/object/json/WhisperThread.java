package glitch.pubsub.object.json;

import lombok.Data;

import java.time.Instant;

@Data
public class WhisperThread {
    private final boolean archived;
    private final boolean muted;
    private final Instant whitelistedUntil;
}
