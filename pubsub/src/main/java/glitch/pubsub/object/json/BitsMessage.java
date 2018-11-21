package glitch.pubsub.object.json;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.Instant;

@Data
public class BitsMessage {
    @SerializedName("user_name")
    private final String username;
    private final String channelName;
    private final Long userId;
    private final Long channelId;
    private final Instant time;
    @SerializedName("chat_message")
    private final String message;
    private final Integer bitsUsed;
    private final Integer totalBitsUsed;
    private final String context;
}
