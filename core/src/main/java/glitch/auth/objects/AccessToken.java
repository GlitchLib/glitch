package glitch.auth.objects;

import com.google.gson.annotations.SerializedName;
import glitch.auth.Scope;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Set;

public interface AccessToken {
    String getAccessToken();

    String getRefreshToken();

    @Nullable
    @SerializedName("expires_in")
    Instant expiredAt();

    @SerializedName("scope")
    Set<Scope> getScopes();
}
