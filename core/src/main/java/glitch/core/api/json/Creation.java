package glitch.core.api.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.core.utils.Immutable;
import java.time.Instant;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = CreationBuilder.class)
public interface Creation {
    Instant createdAt();
}
