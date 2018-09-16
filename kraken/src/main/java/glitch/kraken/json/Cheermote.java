package glitch.kraken.json;

import glitch.core.utils.Immutable;
import java.awt.Color;
import java.util.Map;
import java.util.Set;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Cheermote {
    Background getBackgrounds();
    String getPrefix();
    Set<State> getScales();
    State getStates();
    Set<Tier> getTiers();

    enum Background {
        LIGHT,
        DARK
    }

    enum State {
        STATIC,
        ANIMATED
    }

    @Immutable
    @Value.Immutable
    interface Tier {
        Color getColor();
        Map<Background, Map<State, Map<Double, String>>> getImages();
        Long getMinBits();
    }
}
