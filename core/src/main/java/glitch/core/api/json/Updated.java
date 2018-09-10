package glitch.core.api.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.core.utils.Immutable;
import java.time.Instant;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = UpdatedBuilder.class)
public interface Updated {
    Instant updatedAt();
}
