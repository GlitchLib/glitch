package glitch.chat.api.json;

import com.google.gson.annotations.SerializedName;
import glitch.core.api.json.IDObject;
import glitch.core.api.json.enums.UserType;
import java.time.Instant;
import lombok.Data;

@Data
public class User implements IDObject<Long> {
    private final Long id;
    private final String bio;
    private final Instant createdAt;
    private final String displayName;
    private final String logo;
    @SerializedName(value = "username", alternate = {"name", "login"})
    private final String username;
    private final UserType type;
    private final Instant updatedAt;
}
