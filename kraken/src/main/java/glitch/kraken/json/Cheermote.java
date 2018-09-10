package glitch.kraken.json;

import java.awt.Color;
import java.util.Map;
import java.util.Set;

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

    interface Tier {
        Color getColor();
        Map<Background, Map<State, Map<Double, String>>> getImages();
        Long getMinBits();
    }
}
