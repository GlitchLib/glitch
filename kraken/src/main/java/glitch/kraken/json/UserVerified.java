package glitch.kraken.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UserVerified extends User {
    String getEmail();

    @JsonProperty("email_verified")
    boolean hasEmailVerified();

    @JsonProperty("notifications.email")
    boolean hasEmailNotifications();

    @JsonProperty("notifications.push")
    boolean hasPushNotifications();

    boolean isPartnered();

    @JsonProperty("twitter_connected")
    boolean hasTwitterConnected();
}
