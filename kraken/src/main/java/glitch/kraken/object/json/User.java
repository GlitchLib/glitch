package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.enums.UserType;
import glitch.kraken.object.json.interfaces.IUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
public class User implements IUser {
    @SerializedName(value = "id", alternate = "_id")
    private final Long id;
    @SerializedName(value = "username", alternate = {"login", "name"})
    private final String username;
    private final String displayName;

    private final Instant createdAt;
    private final Instant updatedAt;

    private final String logo;

    @SerializedName(value = "bio", alternate = {"description"})
    private final String userBio;

    private final UserType userType;
}
