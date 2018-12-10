package glitch.helix.object;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class HelixUtils {
    public static String toRfc3339(Instant timestamp) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(timestamp);
    }
}
