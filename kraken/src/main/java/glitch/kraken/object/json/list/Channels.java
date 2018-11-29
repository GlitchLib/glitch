package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Channel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class Channels implements OrdinalList<Channel> {
    private final ImmutableList<Channel> data;
    @Getter(AccessLevel.NONE)
    @SerializedName("_total")
    private final int total;

    @Override
    public int size() {
        return total;
    }
}
