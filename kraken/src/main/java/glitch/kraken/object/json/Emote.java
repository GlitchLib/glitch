package glitch.kraken.object.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

@Data
public class Emote implements IDObject<Long> {
    private final Long id;
    @SerializedName("regex")
    private final String name;

    private final ImmutableList<EmoteImage> images;

    @Data
    public static class EmoteImage {
        private final int width;
        private final int height;
        private final String url;
        @SerializedName("emoticon_set")
        private final Long emoteSet;
    }
}
