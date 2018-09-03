package glitch.kraken.json;

import glitch.utils.Immutable;
import java.time.Instant;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Updated {
    Instant updatedAt();
}
