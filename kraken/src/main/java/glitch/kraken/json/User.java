package glitch.kraken.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import glitch.core.api.json.Creation;
import glitch.core.api.json.IDObject;
import glitch.core.api.json.Updated;
import glitch.core.api.json.enums.UserType;

public interface User extends IDObject<Long>, Creation, Updated {
    String getBio();

    @JsonAlias({"username", "name"})
    String getUsername();

    String getDisplayName();

    String getLogo();

    @JsonProperty("type")
    UserType getUserType();
}
