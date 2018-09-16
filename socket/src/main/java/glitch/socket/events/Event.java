package glitch.socket.events;

import glitch.socket.GlitchWebSocket;
import glitch.socket.utils.EventImmutable;
import java.time.Instant;
import java.util.UUID;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface Event<S extends GlitchWebSocket> {
    default Instant getCreatedAt() {
        return Instant.now();
    }

    default String getEventId() {
        return UUID.randomUUID().toString();
    }

    S getClient();
}
