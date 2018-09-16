package glitch.kraken.json;

import com.google.gson.annotations.SerializedName;
import glitch.core.api.json.Creation;
import glitch.core.api.json.IDObject;
import glitch.core.api.json.Updated;
import glitch.core.api.json.enums.UserType;
import glitch.core.utils.Immutable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface User extends IDObject<Long>, Creation, Updated {
    String getBio();

    @SerializedName(value = "name", alternate = {"username", "login"})
    String getUsername();

    String getDisplayName();

    String getLogo();

    @SerializedName("type")
    UserType getUserType();
}
