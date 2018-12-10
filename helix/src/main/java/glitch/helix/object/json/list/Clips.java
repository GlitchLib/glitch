package glitch.helix.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.CursorList;
import glitch.helix.object.json.Clip;
import lombok.Data;

import javax.annotation.Nullable;

@Data
public class Clips implements CursorList<Clip> {
    private final ImmutableList<Clip> data;

    @Nullable
    @SerializedName("pagination.cursor")
    private final String cursor;
}
