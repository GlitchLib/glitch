package glitch.events;

import glitch.utils.Immutable;
import java.time.Instant;
import java.util.UUID;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Event {
    default Instant getCreatedAt() {
        return Instant.now();
    }

    default String getEventId() {
        return UUID.randomUUID().toString();
    }
}
