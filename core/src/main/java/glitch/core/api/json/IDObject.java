package glitch.core.api.json;

import com.google.gson.annotations.SerializedName;
import glitch.core.utils.Immutable;
import java.io.Serializable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface IDObject<T extends Serializable> {
    @SerializedName(value = "id", alternate = "_id")
    T getId();
}
