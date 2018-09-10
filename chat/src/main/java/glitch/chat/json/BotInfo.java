package glitch.chat.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.core.utils.Immutable;
import java.awt.Color;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = BotInfoBuilder.class)
public interface BotInfo {
    boolean isKnownBot();

    boolean isVerifiedBot();

    Color color();
}
