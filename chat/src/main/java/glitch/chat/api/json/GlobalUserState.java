package glitch.chat.api.json;

import com.google.gson.annotations.SerializedName;
import glitch.core.api.json.Badge;
import glitch.core.api.json.IDObject;
import java.awt.Color;
import java.util.Set;
import lombok.Data;

@Data
public class GlobalUserState implements IDObject<Long> {
    private final Long id;
    private final String login;
    private final String displayName;
    private final Color color;
    @SerializedName("is_verified_bot")
    private final boolean verifiedBot;
    @SerializedName("is_known_bot")
    private final boolean knownBot;
    private final Set<Badge> badges;
}
