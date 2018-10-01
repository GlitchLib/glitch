package glitch.socket.events;

import glitch.socket.GlitchWebSocket;
import glitch.socket.utils.EventImmutable;
import java.time.Instant;
import java.util.UUID;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters
public interface Event<S extends GlitchWebSocket> {
    default Instant getCreatedAt() {
        return Instant.now();
    }

    @Value.Lazy
    default UUID getEventId() {
        return UUID.randomUUID();
    }

    S getClient();
}
