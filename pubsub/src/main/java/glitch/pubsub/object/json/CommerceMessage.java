package glitch.pubsub.object.json;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.Instant;

@Data
public class CommerceMessage {
    @SerializedName("user_name")
    private final String username;
    private final String displayName;
    private final String channelName;
    private final Long userId;
    private final Long channelId;
    private final Instant time;
    private final String itemImageUrl;
    private final String itemDescription;
    private final boolean supportsChannel;
    private final String purchaseMessage;
}
