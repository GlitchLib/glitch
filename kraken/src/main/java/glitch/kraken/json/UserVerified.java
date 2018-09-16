package glitch.kraken.json;

import com.google.gson.annotations.SerializedName;
import glitch.core.utils.Immutable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface UserVerified extends User {
    String getEmail();

    @SerializedName("email_verified")
    boolean hasEmailVerified();

    @SerializedName("notifications.email")
    boolean hasEmailNotifications();

    @SerializedName("notifications.push")
    boolean hasPushNotifications();

    boolean isPartnered();

    @SerializedName("twitter_connected")
    boolean hasTwitterConnected();
}
