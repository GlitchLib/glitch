package glitch.kraken.object.json;

import lombok.Data;

@Data
public class FeatureStream {
    private final String image;
    private final int priority;
    private final boolean scheduled;
    private final boolean sponsored;
    private final Stream stream;
    private final String text;
    private final String title;
}
