package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.enums.SubscriptionType;
import glitch.api.objects.json.interfaces.Creation;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import java.time.Instant;

@Data
public class Subscriber implements IDObject<String>, Creation {
    private final String id;
    private final Instant createdAt;
    @SerializedName("sub_plan")
    private final SubscriptionType subscriptionType;
    @SerializedName("sub_plan_name")
    private final String subscriptionName;
    private final User user;
}
