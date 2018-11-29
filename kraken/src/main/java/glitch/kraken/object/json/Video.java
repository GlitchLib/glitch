package glitch.kraken.object.json;

import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.Creation;
import glitch.api.objects.json.interfaces.IDObject;
import glitch.kraken.object.adapters.BroadcastTypeAdapter;
import glitch.kraken.object.adapters.VideoIdAdapter;
import glitch.kraken.object.adapters.VideoStatusAdapter;
import glitch.kraken.object.adapters.VideoViewTypeAdapter;
import lombok.Data;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Locale;

@Data
public class Video implements IDObject<Long>, Creation {
    @JsonAdapter(VideoIdAdapter.class)
    private final Long id;
    private final Long broadcastId;
    @JsonAdapter(BroadcastTypeAdapter.class)
    @SerializedName("broadcast_type")
    private final Type broadcastType;
    private final Channel channel;
    private final Instant createdAt;

    private final String description;
    private final String descriptionHtml;
    private final ImmutableMap<String, Float> fps;

    private final String game;
    private final Locale language;
    private final Image preview;
    private final Instant publishedAt;
    private final ImmutableMap<String, String> resolutions;
    @JsonAdapter(VideoStatusAdapter.class)
    private final Status status;
    private final String tagList;
    private final Thumbnails thumbnails;
    private final String title;
    private final String url;
    @SerializedName("viewable")
    @JsonAdapter(VideoViewTypeAdapter.class)
    private final ViewType viewType;
    @Nullable
    private final Instant viewableAt;
    private final int views;

    @Data
    public static class Thumbnails {
        private final Thumbnail large;
        private final Thumbnail medium;
        private final Thumbnail small;
        private final Thumbnail template;

        public Thumbnail getCustomSize(int width, int height) {
            return new Thumbnail(template.type, template.url.replace("{width}", Integer.toString(width))
                    .replace("{height}", Integer.toString(height)));
        }

        @Data
        public static class Thumbnail {
            private final String type;
            private final String url;
        }
    }

    public enum Type {
        ARCHIVE, HIGHLIGHT, UPLOAD
    }

    public enum Status {
        CREATED,
        RECORDING,
        RECORDED
    }

    public enum ViewType {
        PUBLIC,
        PRIVATE
    }
}
