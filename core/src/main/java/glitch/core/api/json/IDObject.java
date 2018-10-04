package glitch.core.api.json;

import com.google.gson.annotations.SerializedName;
import glitch.core.utils.Immutable;
import java.io.Serializable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

/**
 * Object companion, contains {@link java.io.Serializable} ID
 * @param <T> the part extending {@link java.io.Serializable}
 *
 * @author Damian Staszewski
 * @since 0.1.0
 */
@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface IDObject<T extends Serializable> {

    /**
     * Gets ID
     * @return ID of the Object
     */
    @SerializedName(value = "id", alternate = "_id")
    T getId();
}
