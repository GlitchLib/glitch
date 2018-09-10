package glitch.core.api.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.core.utils.Immutable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = BadgeBuilder.class)
public interface Badge {
    int getVersion();

    String getName();
}
