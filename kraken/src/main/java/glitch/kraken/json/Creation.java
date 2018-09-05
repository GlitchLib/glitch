package glitch.kraken.json;

import glitch.common.utils.Immutable;
import java.time.Instant;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Creation {
    Instant createdAt();
}
