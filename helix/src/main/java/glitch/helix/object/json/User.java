package glitch.helix.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.enums.BroadcasterType;
import glitch.api.objects.enums.UserType;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import javax.annotation.Nullable;

@Data
public class User implements IDObject<Long> {
    private final BroadcasterType broadcasterType;
    private final String description;
    private final String displayName;
    private final Long id;
    @SerializedName("login")
    private final String username;
    private final String offlineImageUrl;
    private final String profileImageUrl;
    private final UserType type;
    private final long viewCount;

    @Nullable
    private final String email;
}
