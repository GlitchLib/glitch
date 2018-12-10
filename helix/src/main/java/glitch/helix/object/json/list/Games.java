package glitch.helix.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.CursorList;
import glitch.helix.object.json.Game;
import lombok.Data;

@Data
public class Games implements CursorList<Game> {
    private final ImmutableList<Game> data;

    @SerializedName("pagination.cursor")
    private final String cursor;
}
