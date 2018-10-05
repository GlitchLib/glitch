package glitch.chat.api.json;

import com.google.gson.annotations.SerializedName;
import glitch.core.api.json.IDObject;
import lombok.Data;

@Data
public class ChatRoom implements IDObject<String> {
    private final String id;
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
