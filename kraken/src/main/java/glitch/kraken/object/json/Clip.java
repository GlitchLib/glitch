package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.Creation;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import java.time.Instant;
import java.util.Locale;

@Data
public class Clip implements IDObject<String>, Creation {
    @SerializedName("slug")
    private final String id;
    private final long trackingId;
    private final String url;
    private final String embedUrl;
    private final String embedHtml;
    private final ClipUser broadcaster;
    private final ClipUser curator;
    private final VOD vod;
    private final String game;
    private final Locale language;
    private final String title;
    private final int views;
    private final float duration;
    private final Instant createdAt;
    private final Thumbnalis thumbnalis;

    @Data
    public static class ClipUser implements IDObject<Long> {
        private final Long id;
        @SerializedName("name")
        private final String username;
        private final String displayName;
        private final String channelUrl;
        private final String logo;
    }

    @Data
    public static class VOD implements IDObject<Long> {
        private final Long id;
        private final String url;
    }

    @Data
    public static class Thumbnalis {
        private final String medium;
        private final String small;
        private final String tiny;
    }
}
