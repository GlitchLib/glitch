package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.CursorList;
import glitch.kraken.object.json.Clip;
import lombok.Data;

@Data
public class Clips implements CursorList<Clip> {
    private final ImmutableList<Clip> data;
    @SerializedName("_cursor")
    private final String cursor;
}
