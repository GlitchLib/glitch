package glitch.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.utils.Immutable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = BadgeBuilder.class)
public interface Badge {
    int getVersion();
    String getName();
}
