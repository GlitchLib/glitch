package glitch.kraken.object.json;

import com.google.common.collect.ImmutableSet;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.Badge;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import java.awt.*;

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
    private final ImmutableSet<Badge> badges;
}
