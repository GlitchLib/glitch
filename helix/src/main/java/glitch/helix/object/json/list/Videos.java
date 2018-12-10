package glitch.helix.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.CursorList;
import glitch.helix.object.json.Video;
import lombok.Data;

@Data
public class Videos implements CursorList<Video> {
    private final ImmutableList<Video> data;

    @SerializedName("pagination.cursor")
    private final String cursor;
}
