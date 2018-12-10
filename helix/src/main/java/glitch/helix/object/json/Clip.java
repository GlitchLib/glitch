package glitch.helix.object.json;

import glitch.api.objects.json.interfaces.Creation;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import java.time.Instant;
import java.util.Locale;

@Data
public class Clip implements IDObject<String>, Creation {
    private final String id;
    private final String url;
    private final String embedUrl;
    private final long broadcasterId;
    private final String broadcasterName;
    private final long creatorId;
    private final String creatorName;
    private final long videoId;
    private final long gameId;
    private final Locale language;
    private final String title;
    private final int viewCount;
    private final Instant createdAt;
    private final String thumbnailUrl;
}
