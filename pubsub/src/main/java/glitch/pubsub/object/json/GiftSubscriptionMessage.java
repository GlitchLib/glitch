package glitch.pubsub.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.enums.SubscriptionType;
import glitch.pubsub.object.enums.SubscriptionContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class GiftSubscriptionMessage extends SubscriptionMessage {
    private final Long recipientId;
    @SerializedName("recipient_user_name")
    private final String recipientUsername;
    private final String recipientDisplayName;

    public GiftSubscriptionMessage(String username, String displayName, String channelName, Long userId, Long channelId, Instant time, SubscriptionType subscriptionType, String subscriptionName, Integer months, SubscriptionContext context, OrdinalMessage message, Long recipientId, String recipientUsername, String recipientDisplayName) {
        super(username, displayName, channelName, userId, channelId, time, subscriptionType, subscriptionName, months, context, message);
        this.recipientId = recipientId;
        this.recipientUsername = recipientUsername;
        this.recipientDisplayName = recipientDisplayName;
    }
}
