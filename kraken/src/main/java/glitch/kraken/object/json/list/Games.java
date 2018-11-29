package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Game;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class Games implements OrdinalList<Game> {
    private final ImmutableList<Game> data;
    @SerializedName("_total")
    @Getter(AccessLevel.NONE)
    private final int total;

    @Override
    public int size() {
        return total;
    }
}
