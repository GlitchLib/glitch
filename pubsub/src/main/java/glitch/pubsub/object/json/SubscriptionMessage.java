package glitch.pubsub.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.enums.SubscriptionType;
import glitch.pubsub.object.enums.SubscriptionContext;
import lombok.Data;

import java.time.Instant;

@Data
public class SubscriptionMessage {
    @SerializedName("user_name")
    private final String username;
    private final String displayName;
    private final String channelName;
    private final Long userId;
    private final Long channelId;
    private final Instant time;
    @SerializedName("sub_plan")
    private final SubscriptionType subscriptionType;
    @SerializedName("sub_plan_name")
    private final String subscriptionName;
    private final Integer months;
    private final SubscriptionContext context;
    @SerializedName("sub_message")
    private final OrdinalMessage message;
}
