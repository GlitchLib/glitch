package glitch.helix.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.CursorList;
import glitch.helix.object.json.Stream;
import lombok.Data;

@Data
public class Streams implements CursorList<Stream> {
    private final ImmutableList<Stream> data;

    @SerializedName("pagination.cursor")
    private final String cursor;
}
