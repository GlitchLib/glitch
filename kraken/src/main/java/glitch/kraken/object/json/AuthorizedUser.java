package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.enums.UserType;
import glitch.kraken.object.json.interfaces.IAuthorizedUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthorizedUser extends User implements IAuthorizedUser {
    private final String email;
    private final boolean emailVerified;
    private final boolean partnered;
    private final boolean twitterConnected;
    @SerializedName("notifications.push")
    private final boolean pushNotification;
    @SerializedName("notifications.email")
    private final boolean emailNotification;

    public AuthorizedUser(Long id, String username, String displayName, Instant createdAt, Instant updatedAt, String logo, String userBio, UserType userType, String email, boolean emailVerified, boolean partnered, boolean twitterConnected, boolean pushNotification, boolean emailNotification) {
        super(id, username, displayName, createdAt, updatedAt, logo, userBio, userType);
        this.email = email;
        this.emailVerified = emailVerified;
        this.partnered = partnered;
        this.twitterConnected = twitterConnected;
        this.pushNotification = pushNotification;
        this.emailNotification = emailNotification;
    }
}
