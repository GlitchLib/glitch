package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.CursorList;
import glitch.kraken.object.json.Follow;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class Followers implements CursorList<Follow> {
    private final ImmutableList<Follow> data;
    @SerializedName("_cursor")
    private final String cursor;
    @SerializedName("_total")
    @Getter(AccessLevel.NONE)
    private final int total;

    @Override
    public int size() {
        return total;
    }
}
