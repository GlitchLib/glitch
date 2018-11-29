package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Stream;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class Streams implements OrdinalList<Stream> {
    @SerializedName("_total")
    @Getter(AccessLevel.NONE)
    private final int total;
    private final ImmutableList<Stream> data;

    @Override
    public int size() {
        return total;
    }
}
