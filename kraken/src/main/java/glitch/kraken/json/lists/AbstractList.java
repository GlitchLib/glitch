package glitch.kraken.json.lists;

import com.google.gson.annotations.SerializedName;
import glitch.core.utils.Immutable;
import java.util.List;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface AbstractList<T> {
    @SerializedName(value = "data", alternate = {
            "actions"
    })
    List<T> getData();
}
