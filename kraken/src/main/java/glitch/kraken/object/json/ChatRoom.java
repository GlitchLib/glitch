package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import java.util.UUID;

@Data
public class ChatRoom implements IDObject<UUID> {
    private final UUID id;
    private final Long ownerId;
    private final String name;
    private final String topic;
    @SerializedName("is_previewable")
    private final boolean previewable;
    private final Role minimumAllowedRole;

    public enum Role {
        EVERYONE,
        SUBSCRIBER,
        MODERATOR
    }
}
