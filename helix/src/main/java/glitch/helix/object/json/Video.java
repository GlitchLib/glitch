package glitch.helix.object.json;

import com.google.gson.annotations.JsonAdapter;
import glitch.api.objects.adapters.VideoTypeAdapter;
import glitch.api.objects.adapters.VideoViewTypeAdapter;
import glitch.api.objects.enums.VideoType;
import glitch.api.objects.enums.ViewType;
import glitch.api.objects.json.interfaces.Creation;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import java.time.Instant;
import java.util.Locale;

@Data
public class Video implements IDObject<Long>, Creation {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final String title;
    private final String description;
    private final Instant createdAt;
    private final Instant publishedAt;
    private final String url;
    private final String thumbnailUrl;
    @JsonAdapter(VideoViewTypeAdapter.class)
    private final ViewType viewable;
    private final int viewCount;
    private final Locale language;
    @JsonAdapter(VideoTypeAdapter.class)
    private final VideoType type;
}
