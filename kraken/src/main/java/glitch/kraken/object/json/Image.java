package glitch.kraken.object.json;

import lombok.Data;

@Data
public class Image {
    private final String large;
    private final String medium;
    private final String small;
    private final String template;

    public String getCustomSize(int width, int height) {
        return template.replace("{width}", Integer.toString(width))
                .replace("{height}", Integer.toString(height));
    }
}