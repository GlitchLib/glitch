package glitch.helix.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.CursorList;
import glitch.helix.object.json.Follow;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.annotation.Nullable;

@Data
public class Follows implements CursorList<Follow> {
    private final ImmutableList<Follow> data;
    @Getter(AccessLevel.NONE)
    private final int total;

    @Nullable
    @SerializedName("pagination.cursor")
    private final String cursor;

    @Override
    public int size() {
        return total;
    }
}
