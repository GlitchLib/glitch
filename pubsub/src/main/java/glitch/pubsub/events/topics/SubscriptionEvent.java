package glitch.pubsub.events.topics;

import com.google.gson.annotations.SerializedName;
import glitch.core.api.json.enums.SubscriptionType;
import glitch.pubsub.GlitchPubSub;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import java.time.Instant;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface SubscriptionEvent extends Event<GlitchPubSub> {
    String getUserName();
    String getDisplayName();
    String getChannelName();
    Long getChannelId();
    Long getUserId();
    Instant getTime();
    @SerializedName("sub_plan")
    SubscriptionType getSubscriptionType();
    @SerializedName("sub_plan_name")
    String getSubscriptionName();
    Integer getMonths();
    SubscriptionContext getContext();
    @SerializedName("sub_message")
    Message getSubscriptionMessage();
}
